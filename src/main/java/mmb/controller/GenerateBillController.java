package mmb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import mmb.dto.GenerateBillDTO;
import mmb.dto.MaterialWithCompanyProjection;
import mmb.dto.OtherWorkDTO;
import mmb.model.GenerateBill;
import mmb.model.GenerateMaterialsBill;
import mmb.model.OtherWork;
import mmb.repository.GenerateBillRepo;
import mmb.repository.MaterialCompanyNameRepo;
import mmb.repository.MaterialTypeRepo;
import mmb.repository.RawMaterialRepo;
import mmb.service.GenerateBillService;
import mmb.serviceImpl.PdfService;

@Controller
@RequestMapping("/generateBills")
@SessionAttributes("token")
public class GenerateBillController {

	private final GenerateBillService billService;
	private final RawMaterialRepo rawMaterialRepository;
	@Autowired
	private MaterialTypeRepo materialTypeRepository;
	@Autowired
	private MaterialCompanyNameRepo materialCompanyNameRepository;
	@Autowired
	private GenerateBillRepo generateBillRepository;

	@Autowired
	public GenerateBillController(GenerateBillService billService, RawMaterialRepo rawMaterialRepository) {
		this.billService = billService;
		this.rawMaterialRepository = rawMaterialRepository;
	}

	@Autowired
	private PdfService pdfService;

	@GetMapping("/getAllBill")
	public String generateBills(Model model) {
		model.addAttribute("generateBills", billService.getAllBills());
		return "generateBill/bill-list";
	}

	@GetMapping("/generateNewBill")
	public String generateNewBill(Model model) {
		List<MaterialWithCompanyProjection> materials = generateBillRepository.findAllMaterialsDtls();
		model.addAttribute("generateBill", new GenerateBillDTO());
		model.addAttribute("materials", materials);
		model.addAttribute("materialTypes", materialTypeRepository.findAll());
		model.addAttribute("companies", materialCompanyNameRepository.findAll());
		return "generateBill/generate-bill-form";
	}

	@PostMapping("/saveGenerateBill")
	public String saveGenerateBill(@ModelAttribute("generateBill") GenerateBillDTO generateBillDTO) {
		billService.saveOrUpdate(generateBillDTO);
		return "redirect:/generateBills/getAllBill";
	}

	@GetMapping("/editBill/{id}")
	public String editBill(@PathVariable Integer id, Model model) {
		GenerateBill bill = billService.getBillById(id);
		List<MaterialWithCompanyProjection> materials = generateBillRepository.findAllMaterialsDtls();

		// Convert entity to DTO
		GenerateBillDTO dto = new GenerateBillDTO();
		dto.setBillId(bill.getBillId());
		dto.setCustomerName(bill.getCustomerName());
		dto.setMobileNo(bill.getMobileNo());
		dto.setWorkAddress(bill.getWorkAddress());
		dto.setWorkDate(bill.getWorkDate());
		dto.setBoringType(bill.getBoringType());
		dto.setBoringDia(bill.getBoringDia());
		dto.setPriceQntDtls(bill.getPriceQntDtls());
		dto.setDrillingPrice(bill.getDrillingPrice());
		dto.setTransportingVehicleType(bill.getTransportingVehicleType());
		dto.setTransportingPrice(bill.getTransportingPrice());
		dto.setTotalDrilling(bill.getTotalDrilling());

//        dto.setRequiredMaterialIds(bill.getRequiredMaterials()
//                .stream().map(RawMaterial::getMaterialId).toList());
		// ✅ Map materials to requiredMaterialIds + Quantities
		if (bill.getMaterialsBill() != null) {
		    dto.setRequiredMaterialIds(
		            bill.getMaterialsBill().stream()
		                .map(gmb -> gmb.getRawMaterial().getMaterialId())
		                .toList()
		    );

		    dto.setRequiredMaterialQuantities(
		            bill.getMaterialsBill().stream()
		                .map(GenerateMaterialsBill::getTotalUnit)
		                .toList()
		    );

		    // 🔎 Print for debugging
		    System.out.println("✅ DTO RequiredMaterialIds: " + dto.getRequiredMaterialIds());
		    System.out.println("✅ DTO RequiredMaterialQuantities: " + dto.getRequiredMaterialQuantities());

		    // If you want one-to-one mapping print
		    for (int i = 0; i < dto.getRequiredMaterialIds().size(); i++) {
		        System.out.println("MaterialId: " + dto.getRequiredMaterialIds().get(i) +
		                           ", Quantity: " + dto.getRequiredMaterialQuantities().get(i));
		    }
		}

		// ✅ Map other works
		if (bill.getOtherWorks() != null) {
		    System.out.println("🔎 Other Works from DB:");
		    for (OtherWork ow : bill.getOtherWorks()) {
		        System.out.println("ID: " + ow.getOthWorkId() +
		                           ", Name: " + ow.getOthWorkName() +
		                           ", Unit: " + ow.getTotalUnit() +
		                           ", Price: " + ow.getPrice());
		    }

		    dto.setOtherWorks(
		        bill.getOtherWorks().stream()
		            .map(ow -> new OtherWorkDTO(
		                    ow.getOthWorkId(),
		                    ow.getOthWorkName(),
		                    ow.getTotalUnit(),
		                    ow.getPrice()))
		            .toList()
		    );

		    // 🔎 Print DTO after mapping
		    System.out.println("✅ DTO OtherWorks: " + dto.getOtherWorks());
		}

		model.addAttribute("generateBill", dto);
		model.addAttribute("materials", materials);
		model.addAttribute("materialTypes", materialTypeRepository.findAll());
		model.addAttribute("companies", materialCompanyNameRepository.findAll());
		return "generateBill/edit-generate-bill";
	}

	@GetMapping("/delete/{id}")
	public String deleteBill(@PathVariable Integer id) {
		billService.deleteBillById(id);
		return "redirect:/generateBills/getAllBill";
	}
}

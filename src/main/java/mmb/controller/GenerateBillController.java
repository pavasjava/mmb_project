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

import jakarta.servlet.http.HttpServletResponse;
import mmb.dto.GenerateBillDTO;
import mmb.dto.MaterialWithCompanyProjection;
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
		GenerateBillDTO dto = billService.getBillById(id);

		model.addAttribute("generateBill", dto);
		model.addAttribute("materials", billService.getAllMaterials());
		model.addAttribute("materialTypes", billService.getAllMaterialTypes());
		model.addAttribute("companies", billService.getAllCompanies());

		return "generateBill/edit-generate-bill";
	}

	@GetMapping("/downloadBillPdf/{id}")
	public void downloadPdf(@PathVariable("id") Integer id, HttpServletResponse response) {
		try {
	        // 1. Fetch DTO
	        GenerateBillDTO bill = billService.getBillById(id);

	        // 2. Set response headers
	        response.setContentType("application/pdf");
	        response.setHeader("Content-Disposition", "attachment; filename=bill_" + id + ".pdf");

	        // 3. Generate PDF into response output stream
	        billService.generateBillPdf(bill, response.getOutputStream());

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error while downloading bill PDF", e);
	    }
	}

	@GetMapping("/delete/{id}")
	public String deleteBill(@PathVariable Integer id) {
		billService.deleteBillById(id);
		return "redirect:/generateBills/getAllBill";
	}
}

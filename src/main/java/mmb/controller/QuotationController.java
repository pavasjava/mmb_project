package mmb.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.lowagie.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;
import mmb.dto.MaterialWithCompanyProjection;
import mmb.dto.QuotationDTO;
import mmb.model.Quotation;
import mmb.model.RawMaterial;
import mmb.repository.MaterialCompanyNameRepo;
import mmb.repository.MaterialTypeRepo;
import mmb.repository.QuotationRepo;
import mmb.repository.RawMaterialRepo;
import mmb.service.QuotationService;
import mmb.serviceImpl.PdfService;

@Controller
@RequestMapping("/quotations")
@SessionAttributes("token")
public class QuotationController {

	private final QuotationService quotationService;
	private final RawMaterialRepo rawMaterialRepository;
	@Autowired
	private MaterialTypeRepo materialTypeRepository;
	@Autowired
	private MaterialCompanyNameRepo materialCompanyNameRepository;
	@Autowired
	private QuotationRepo quotationRepository;

	@Autowired
	public QuotationController(QuotationService quotationService, RawMaterialRepo rawMaterialRepository) {
		this.quotationService = quotationService;
		this.rawMaterialRepository = rawMaterialRepository;
	}

	@Autowired
	private PdfService pdfService;

	@GetMapping("/getAllQuotation")
	public String listQuotations(Model model) {
		model.addAttribute("quotations", quotationService.getAllQuotations());
		return "quotation/quotations-list";
	}

	@GetMapping("/addNewQuotation")
	public String addQuotationForm(Model model) {
//        List<RawMaterial> materials = (List<RawMaterial>) rawMaterialRepository.findAll();
		List<MaterialWithCompanyProjection> materials = quotationRepository.findAllMaterialsDtls();
//        model.addAttribute("materials", materials);
		model.addAttribute("quotation", new QuotationDTO());
		model.addAttribute("materials", materials);
		model.addAttribute("materialTypes", materialTypeRepository.findAll());
		model.addAttribute("companies", materialCompanyNameRepository.findAll());
		return "quotation/quotation-form";
	}

	@PostMapping("/saveQuotation")
	public String saveQuotation(@ModelAttribute("quotation") QuotationDTO quotationDTO) {
		quotationService.saveOrUpdate(quotationDTO);
		return "redirect:/quotations/getAllQuotation";
	}

//    @PostMapping("/saveQuotation")
//    public String saveQuotation(@ModelAttribute("quotation") QuotationDTO dto) {
//        // DEBUG (optional)
//        System.out.println("Saving quotation id=" + dto.getQuotationId()
//                + " materials=" + dto.getRequiredMaterialIds());
//        quotationService.saveOrUpdate(dto);
//        return "redirect:/quotations/getAllQuotation";
//    }
//
	@GetMapping("/editQuotation/{id}")
	public String editQuotationForm(@PathVariable("id") Integer id, Model model) {
		Optional<Quotation> optionalQuotation = quotationService.getQuotationById(id);

		if (optionalQuotation.isPresent()) {
			Quotation quotation = optionalQuotation.get();

			QuotationDTO dto = new QuotationDTO();
			dto.setQuotationId(quotation.getQuotationId());
			dto.setCustomerName(quotation.getCustomerName());
			dto.setWorkAddress(quotation.getWorkAddress());
			dto.setBoringType(quotation.getBoringType());
			dto.setBoringDia(quotation.getBoringDia());
			dto.setPriceQntDtls(quotation.getPriceQntDtls());
			dto.setDrillingPrice(quotation.getDrillingPrice());
			dto.setTransportingVehicleType(quotation.getTransportingVehicleType());
			dto.setTransportingPrice(quotation.getTransportingPrice());
			dto.setDoe(quotation.getDoe());

			// collect already selected material ids
			List<Integer> materialIds = quotation.getRequiredMaterials().stream().map(RawMaterial::getMaterialId)
					.toList();
			dto.setRequiredMaterialIds(materialIds);
			System.out.println("Loaded requiredMaterialIds: " + dto.getRequiredMaterialIds());

			// ✅ use projection instead of RawMaterial
			List<MaterialWithCompanyProjection> materials = quotationRepository.findAllMaterialsDtls();

			model.addAttribute("quotation", dto);
			model.addAttribute("materials", materials);

			return "quotation/editQuotation-form";
		} else {
			return "redirect:/quotations/getAllQuotation";
		}
	}

	@GetMapping("/deleteQuotation/{id}")
	public String deleteQuotation(@PathVariable int id) {
		quotationService.deleteQuotation(id);
		return "redirect:/quotations/getAllQuotation";
	}

	@GetMapping("/downloadQuotation/{id}")
	public void downloadQuotationPdf(@PathVariable("id") Integer id, HttpServletResponse response)
	        throws IOException, DocumentException {

	    Optional<Quotation> optionalQuotation = quotationService.getQuotationById(id);

	    if (optionalQuotation.isPresent()) {
	        Quotation quotation = optionalQuotation.get();

	        // Build a safe filename
	        String customerName = quotation.getCustomerName() != null ? quotation.getCustomerName() : "Customer";
	        customerName = customerName.replaceAll("[^a-zA-Z0-9]", "_"); // sanitize special chars

	        String date = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());

	        String fileName = "Quotation_" + quotation.getQuotationId() + "_" + customerName + "_" + date + ".pdf";

	        // Set response headers
	        response.setContentType("application/pdf");
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

	        // Generate PDF
	        pdfService.generateQuotationPdf(response, quotation);

	    } else {
	        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Quotation not found");
	    }
	}


}

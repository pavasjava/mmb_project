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
import mmb.dto.QuotationDTO;
import mmb.model.Quotation;
import mmb.model.RawMaterial;
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
        List<RawMaterial> materials = (List<RawMaterial>) rawMaterialRepository.findAll();
        model.addAttribute("materials", materials);
        model.addAttribute("quotation", new QuotationDTO());  // use DTO
        return "quotation/quotation-form";
    }


    @PostMapping("/saveQuotation")
    public String saveQuotation(@ModelAttribute("quotation") QuotationDTO dto) {
        // DEBUG (optional)
        System.out.println("Saving quotation id=" + dto.getQuotationId()
                + " materials=" + dto.getRequiredMaterialIds());
        quotationService.saveOrUpdate(dto);
        return "redirect:/quotations/getAllQuotation";
    }

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
            dto.setDrillingPrice(quotation.getDrillingPrice());
            dto.setTransportingVehicleType(quotation.getTransportingVehicleType());
            dto.setTransportingPrice(quotation.getTransportingPrice());
            dto.setDoe(quotation.getDoe());

            List<Integer> materialIds = quotation.getRequiredMaterials()
                                                 .stream()
                                                 .map(RawMaterial::getMaterialId)
                                                 .toList();
            System.out.println("Material IDs for Quotation " + id + ": " + materialIds);
            dto.setRequiredMaterialIds(materialIds);

            List<RawMaterial> materials = rawMaterialRepository.findAll();

            model.addAttribute("quotation", dto);
            model.addAttribute("materials", materials);

            return "quotation/quotation-form"; 
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
    public void downloadQuotationPdf(@PathVariable("id") Integer id, HttpServletResponse response) throws IOException, DocumentException {

        Optional<Quotation> optionalQuotation = quotationService.getQuotationById(id);

        if (optionalQuotation.isPresent()) {
            Quotation quotation = optionalQuotation.get();

            // Set response headers
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=downloadPdf_" + quotation.getQuotationId() + ".pdf");

            pdfService.generateQuotationPdf(response, quotation);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Quotation not found");
        }
    }

}

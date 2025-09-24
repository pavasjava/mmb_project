package mmb.restController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lowagie.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;
import mmb.dto.QuotationDTO;
import mmb.model.Quotation;
import mmb.model.RawMaterial;
import mmb.repository.MaterialCompanyNameRepo;
import mmb.repository.MaterialTypeRepo;
import mmb.repository.QuotationRepo;
import mmb.repository.RawMaterialRepo;
import mmb.service.rest.QuotationRestService;
import mmb.serviceImpl.PdfService;

@RestController
@RequestMapping("/api/quotations")
public class QuotationRestController {

    private final QuotationRestService quotationService;
    private final RawMaterialRepo rawMaterialRepository;
    private final MaterialTypeRepo materialTypeRepository;
    private final MaterialCompanyNameRepo materialCompanyNameRepository;
    private final QuotationRepo quotationRepository;
    private final PdfService pdfService;

    @Autowired
    public QuotationRestController(QuotationRestService quotationService,
                               RawMaterialRepo rawMaterialRepository,
                               MaterialTypeRepo materialTypeRepository,
                               MaterialCompanyNameRepo materialCompanyNameRepository,
                               QuotationRepo quotationRepository,
                               PdfService pdfService) {
        this.quotationService = quotationService;
        this.rawMaterialRepository = rawMaterialRepository;
        this.materialTypeRepository = materialTypeRepository;
        this.materialCompanyNameRepository = materialCompanyNameRepository;
        this.quotationRepository = quotationRepository;
        this.pdfService = pdfService;
    }

    @GetMapping("/getAllQuotation")
    public ResponseEntity<List<QuotationDTO>> listQuotations() {
        return ResponseEntity.ok(quotationService.getAllQuotations());
    }

    @PostMapping("/saveQuotation")
    public ResponseEntity<String> saveQuotation(@RequestBody QuotationDTO quotationDTO) {
        quotationService.saveOrUpdate(quotationDTO);
        return ResponseEntity.ok("Quotation saved successfully");
    }

    @GetMapping("/editQuotation/{id}")
    public ResponseEntity<QuotationDTO> editQuotationForm(@PathVariable("id") Integer id) {
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

            List<Integer> materialIds = quotation.getRequiredMaterials().stream()
                    .map(RawMaterial::getMaterialId)
                    .toList();
            dto.setRequiredMaterialIds(materialIds);

            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteQuotation/{id}")
    public ResponseEntity<String> deleteQuotation(@PathVariable int id) {
        quotationService.deleteQuotation(id);
        return ResponseEntity.ok("Quotation deleted successfully with id: " + id);
    }

    @GetMapping("/downloadQuotation/{id}")
    public void downloadQuotationPdf(@PathVariable("id") Integer id, HttpServletResponse response)
            throws IOException, DocumentException {

        Optional<Quotation> optionalQuotation = quotationService.getQuotationById(id);

        if (optionalQuotation.isPresent()) {
            Quotation quotation = optionalQuotation.get();

            String customerName = quotation.getCustomerName() != null ? quotation.getCustomerName() : "Customer";
            customerName = customerName.replaceAll("[^a-zA-Z0-9]", "_");
            String date = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());

            String fileName = "Quotation_" + quotation.getQuotationId() + "_" + customerName + "_" + date + ".pdf";

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            pdfService.generateQuotationPdf(response, quotation);

        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Quotation not found");
        }
    }
    
    @GetMapping("/updateCredentials")
    public ResponseEntity<Map<String, String>> showUpdateCredentials() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is the update credentials page. Implement your forgot password logic here.");
        return ResponseEntity.ok(response);
    }
}

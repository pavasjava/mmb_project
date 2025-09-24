package mmb.restController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import mmb.dto.GenerateBillDTO;
import mmb.mapper.GenerateBillMapper;
import mmb.model.GenerateBill;
import mmb.repository.GenerateBillRepo;
import mmb.repository.MaterialCompanyNameRepo;
import mmb.repository.MaterialTypeRepo;
import mmb.repository.RawMaterialRepo;
import mmb.service.rest.GenerateBillRestService;
import mmb.serviceImpl.PdfService;

@RestController
@RequestMapping("/api/resGenerateBills")
public class GenerateBillRestController {

    private final GenerateBillRestService billService;
    private final RawMaterialRepo rawMaterialRepository;
    private final MaterialTypeRepo materialTypeRepository;
    private final MaterialCompanyNameRepo materialCompanyNameRepository;
    private final GenerateBillRepo generateBillRepository;
    private final PdfService pdfService;
    private final GenerateBillMapper mapper;

    @Autowired
    public GenerateBillRestController(GenerateBillRestService billService,
                                      RawMaterialRepo rawMaterialRepository,
                                      MaterialTypeRepo materialTypeRepository,
                                      MaterialCompanyNameRepo materialCompanyNameRepository,
                                      GenerateBillRepo generateBillRepository,
                                      PdfService pdfService,GenerateBillMapper mapper) {
        this.billService = billService;
        this.rawMaterialRepository = rawMaterialRepository;
        this.materialTypeRepository = materialTypeRepository;
        this.materialCompanyNameRepository = materialCompanyNameRepository;
        this.generateBillRepository = generateBillRepository;
        this.pdfService = pdfService;
        this.mapper = mapper;
    }

    @Autowired
    private ModelMapper modelMapper;
    
    // ✅ Get all bills
    @GetMapping
    public List<GenerateBillDTO> getAllBills() {
    	List<GenerateBill> bills = generateBillRepository.findAll();
        return bills.stream()
//                .map(bill -> modelMapper.map(bill, GenerateBillDTO.class))
        		.map(mapper::mapToDTO)
                .collect(Collectors.toList());
    }
    
    

    // ✅ Get new bill form data (materials, materialTypes, companies)
    @GetMapping("/form-data")
    public Map<String, Object> getFormData() {
        Map<String, Object> response = new HashMap<>();
        response.put("generateBill", new GenerateBillDTO());
        response.put("materials", generateBillRepository.findAllMaterialsDtls());
        response.put("materialTypes", materialTypeRepository.findAll());
        response.put("companies", materialCompanyNameRepository.findAll());
        return response;
    }

    // ✅ Save or update bill
    @PostMapping
    public ResponseEntity<GenerateBillDTO> saveGenerateBill(@RequestBody GenerateBillDTO generateBillDTO) {
    	GenerateBillDTO savedBill = billService.saveOrUpdate(generateBillDTO);
        return ResponseEntity.ok(savedBill);
    }

    // ✅ Get bill by ID
    @GetMapping("/{id}")
    public ResponseEntity<GenerateBillDTO> getBillById(@PathVariable Integer id) {
        GenerateBillDTO bill = billService.getBillById(id);
        return ResponseEntity.ok(bill);
    }

    // ✅ Edit bill (fetch details for editing)
    @GetMapping("/{id}/edit-data")
    public Map<String, Object> getEditBillData(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        response.put("generateBill", billService.getBillById(id));
        response.put("materials", billService.getAllMaterials());
        response.put("materialTypes", billService.getAllMaterialTypes());
        response.put("companies", billService.getAllCompanies());
        return response;
    }

    // ✅ Download bill as PDF
    @GetMapping("/{id}/download")
    public void downloadPdf(@PathVariable Integer id, HttpServletResponse response) {
        try {
            GenerateBillDTO bill = billService.getBillById(id);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=bill_" + id + ".pdf");

            billService.generateBillPdf(bill, response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while downloading bill PDF", e);
        }
    }

    // ✅ Delete bill
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBill(@PathVariable Integer id) {
        billService.deleteBillById(id);
        return ResponseEntity.ok("Bill deleted successfully with id: " + id);
    }
}

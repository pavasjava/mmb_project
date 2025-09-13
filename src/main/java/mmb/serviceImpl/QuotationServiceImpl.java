package mmb.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import mmb.dto.MaterialSelectionDTO;
import mmb.dto.QuotationDTO;
import mmb.model.Quotation;
import mmb.model.RawMaterial;
import mmb.repository.QuotationRepo;
import mmb.repository.RawMaterialRepo;
import mmb.service.QuotationService;

@Service
@Transactional
public class QuotationServiceImpl implements QuotationService {

	private final QuotationRepo quotationRepository;
	private final RawMaterialRepo rawMaterialRepo;

    @Autowired
    public QuotationServiceImpl(QuotationRepo quotationRepository, RawMaterialRepo rawMaterialRepo) {
        this.quotationRepository = quotationRepository;
        this.rawMaterialRepo = rawMaterialRepo;
    }

//    @Override
//    public Quotation saveQuotation(QuotationDTO quotation) {
//        return quotationRepository.save(quotation);
//    }

    @Override
    public List<Quotation> getAllQuotations() {
        return quotationRepository.findAll();
    }

    @Override
    public Optional<Quotation> getQuotationById(Integer id) {
    	return quotationRepository.findByIdWithMaterials(id);
    }

    @Override
    public void deleteQuotation(Integer id) {
        quotationRepository.deleteById(id);
    }
    
//    @Override
//    public Quotation saveQuotation(QuotationDTO dto) {
//        
//    }

//    @Override
//    public Quotation saveOrUpdate(QuotationDTO dto) {
//        Quotation quotation = new Quotation();
//        quotation.setQuotationId(dto.getQuotationId());
//        quotation.setCustomerName(dto.getCustomerName());
//        quotation.setWorkAddress(dto.getWorkAddress());
//        quotation.setBoringType(dto.getBoringType());
//        quotation.setBoringDia(dto.getBoringDia());
//        quotation.setDrillingPrice(dto.getDrillingPrice());
//        quotation.setTransportingVehicleType(dto.getTransportingVehicleType());
//        quotation.setTransportingPrice(dto.getTransportingPrice());
//
//        List<RawMaterial> selectedMaterials = new ArrayList<>();
//
//        // 👉 Here is where you add your block
//        for (MaterialSelectionDTO m : dto.getSelectedMaterials()) {
//            rawMaterialRepo
//                .findByMaterialType_MaterialTypeIdAndCompanyName_CompanyId(m.getMaterialTypeId(), m.getCompanyId())
//                .ifPresent(selectedMaterials::add);
//        }
//
//        quotation.setRequiredMaterials(selectedMaterials);
//
//        return quotationRepository.save(quotation);
//    }

    @Transactional
    public void saveOrUpdate(QuotationDTO dto) {
        Quotation quotation = new Quotation();

        // map simple fields
        quotation.setQuotationId(dto.getQuotationId());
        quotation.setCustomerName(dto.getCustomerName());
        quotation.setWorkAddress(dto.getWorkAddress());
        quotation.setBoringType(dto.getBoringType());
        quotation.setBoringDia(dto.getBoringDia());
        quotation.setPriceQntDtls(dto.getPriceQntDtls());
        quotation.setDrillingPrice(dto.getDrillingPrice());
        quotation.setTransportingVehicleType(dto.getTransportingVehicleType());
        quotation.setTransportingPrice(dto.getTransportingPrice());

        // ✅ map required materials
        if (dto.getRequiredMaterialIds() != null && !dto.getRequiredMaterialIds().isEmpty()) {
            List<RawMaterial> materials = rawMaterialRepo.findAllById(dto.getRequiredMaterialIds());
            quotation.setRequiredMaterials(materials);
        }

        quotationRepository.save(quotation);
    }


}

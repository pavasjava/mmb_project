package mmb.serviceImpl.rest;

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
import mmb.service.rest.QuotationRestService;

@Service
@Transactional
public class QuotationRestServiceImpl implements QuotationRestService {

	private final QuotationRepo quotationRepository;
	private final RawMaterialRepo rawMaterialRepo;

    @Autowired
    public QuotationRestServiceImpl(QuotationRepo quotationRepository, RawMaterialRepo rawMaterialRepo) {
        this.quotationRepository = quotationRepository;
        this.rawMaterialRepo = rawMaterialRepo;
    }

//    @Override
//    public Quotation saveQuotation(QuotationDTO quotation) {
//        return quotationRepository.save(quotation);
//    }

    @Override
    public List<QuotationDTO> getAllQuotations() {
        return quotationRepository.findAll().stream()
                .map(quotation -> {
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

                    // ✅ Map requiredMaterials
                    if (quotation.getRequiredMaterials() != null) {
                        List<Integer> materialIds = quotation.getRequiredMaterials().stream()
                                .map(RawMaterial::getMaterialId)
                                .toList();
                        dto.setRequiredMaterialIds(materialIds);

                        // ✅ Map to MaterialSelectionDTO (using relations)
                        List<MaterialSelectionDTO> selectedMaterials = quotation.getRequiredMaterials().stream()
                        	    .map(mat -> {
                        	        MaterialSelectionDTO ms = new MaterialSelectionDTO();
                        	        ms.setMaterialTypeId(mat.getMaterialType() != null ? mat.getMaterialType().getMaterialTypeId() : null);
                        	        ms.setCompanyId(mat.getCompanyName() != null ? mat.getCompanyName().getCompanyId() : null);
                        	        return ms;
                        	    })
                        	    .toList();
                        dto.setSelectedMaterials(selectedMaterials);
                        dto.setSelectedMaterials(selectedMaterials);
                    }

                    // Only if these fields exist in Quotation
                    dto.setSelectedMaterialTypeId(dto.getSelectedMaterialTypeId());
                    dto.setSelectedCompanyId(dto.getSelectedCompanyId());

                    return dto;
                })
                .toList();
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

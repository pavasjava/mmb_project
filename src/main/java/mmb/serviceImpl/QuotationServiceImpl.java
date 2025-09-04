package mmb.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
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

    @Override
    public Quotation saveQuotation(Quotation quotation) {
        return quotationRepository.save(quotation);
    }

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
    
    public Quotation saveOrUpdate(QuotationDTO dto) {
        final Quotation entity;

        if (dto.getQuotationId() != null) {
            // UPDATE path: load existing (with materials) and mutate it
            entity = quotationRepository.findByIdWithMaterials(dto.getQuotationId())
                    .orElseThrow(() -> new IllegalArgumentException("Quotation not found: " + dto.getQuotationId()));
        } else {
            entity = new Quotation();
        }

        entity.setCustomerName(dto.getCustomerName());
        entity.setWorkAddress(dto.getWorkAddress());
        entity.setBoringType(dto.getBoringType());
        entity.setBoringDia(dto.getBoringDia());
        entity.setDrillingPrice(dto.getDrillingPrice());
        entity.setTransportingVehicleType(dto.getTransportingVehicleType());
        entity.setTransportingPrice(dto.getTransportingPrice());
        entity.getRequiredMaterials().clear();
        if (dto.getRequiredMaterialIds() != null && !dto.getRequiredMaterialIds().isEmpty()) {
            List<RawMaterial> selected = rawMaterialRepo.findAllById(dto.getRequiredMaterialIds());
            entity.getRequiredMaterials().addAll(selected);
        }

        return quotationRepository.save(entity);
    }

}

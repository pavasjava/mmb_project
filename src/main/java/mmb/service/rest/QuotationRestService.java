package mmb.service.rest;

import java.util.List;
import java.util.Optional;

import mmb.dto.QuotationDTO;
import mmb.model.Quotation;

public interface QuotationRestService {

	List<QuotationDTO> getAllQuotations();
    Optional<Quotation> getQuotationById(Integer id);
    void deleteQuotation(Integer id);
    public void saveOrUpdate(QuotationDTO dto);
}

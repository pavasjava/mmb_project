package mmb.service;

import java.util.List;
import java.util.Optional;

import mmb.dto.QuotationDTO;
import mmb.model.Quotation;

public interface QuotationService {
	
	Quotation saveQuotation(Quotation quotation);
    List<Quotation> getAllQuotations();
    Optional<Quotation> getQuotationById(Integer id);
    void deleteQuotation(Integer id);
    public Quotation saveOrUpdate(QuotationDTO dto);
}

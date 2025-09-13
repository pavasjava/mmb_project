package mmb.service;

import java.util.List;
import java.util.Optional;

import mmb.dto.GenerateBillDTO;
import mmb.model.GenerateBill;

public interface GenerateBillService {

	List<GenerateBill> getAllBills();
    GenerateBill getBillById(Integer id);
    void deleteBillById(Integer id);
    public GenerateBill saveOrUpdate(GenerateBillDTO dto);
}

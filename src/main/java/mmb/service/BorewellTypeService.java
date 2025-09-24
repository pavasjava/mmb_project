package mmb.service;

import java.util.List;

import mmb.dto.BorewellTypeDTO;
import mmb.model.BorewellType;

public interface BorewellTypeService {
	
	public BorewellTypeDTO saveBorewellType(BorewellTypeDTO dto);
	public List<BorewellTypeDTO> getAllTypes();
	BorewellTypeDTO getById(Long id);
    void deleteById(Long id);

}

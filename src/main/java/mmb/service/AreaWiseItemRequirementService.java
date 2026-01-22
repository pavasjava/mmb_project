package mmb.service;

import java.util.List;

import mmb.dto.AreaWiseItemRequirementDTO;

public interface AreaWiseItemRequirementService {
	
	List<AreaWiseItemRequirementDTO> getAllRequirements();

    AreaWiseItemRequirementDTO getRequirementById(Long id);

    AreaWiseItemRequirementDTO saveRequirement(AreaWiseItemRequirementDTO dto);

    AreaWiseItemRequirementDTO updateRequirement(Long id, AreaWiseItemRequirementDTO dto);

    void deleteRequirement(Long id);
    
    AreaWiseItemRequirementDTO getRequirementItemDetails(String cityId, String locationAreaId, String borewellTypeId, String drillingSize);
    
    
}
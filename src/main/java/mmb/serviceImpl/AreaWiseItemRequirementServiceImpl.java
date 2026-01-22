package mmb.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mmb.dto.AreaWiseItemRequirementDTO;
import mmb.model.AreaWiseItemRequirement;
import mmb.repository.AreaWiseItemRequirementRepository;
import mmb.service.AreaWiseItemRequirementService;

@Service
public class AreaWiseItemRequirementServiceImpl implements AreaWiseItemRequirementService {

	@Autowired
	private AreaWiseItemRequirementRepository repository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<AreaWiseItemRequirementDTO> getAllRequirements() {
		return repository.findAll().stream().map(req -> modelMapper.map(req, AreaWiseItemRequirementDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public AreaWiseItemRequirementDTO getRequirementById(Long id) {
		AreaWiseItemRequirement requirement = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Requirement not found with ID: " + id));
		return modelMapper.map(requirement, AreaWiseItemRequirementDTO.class);
	}

	@Override
	public AreaWiseItemRequirementDTO saveRequirement(AreaWiseItemRequirementDTO dto) {
		AreaWiseItemRequirement entity = modelMapper.map(dto, AreaWiseItemRequirement.class);
		AreaWiseItemRequirement saved = repository.save(entity);
		return modelMapper.map(saved, AreaWiseItemRequirementDTO.class);
	}

	@Override
	public AreaWiseItemRequirementDTO updateRequirement(Long id, AreaWiseItemRequirementDTO dto) {
		AreaWiseItemRequirement existing = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Requirement not found with ID: " + id));

		existing.setReqCasingPipe(dto.getReqCasingPipe());
		existing.setReqMasterCasingPipe(dto.getReqMasterCasingPipe());
		existing.setNoOfSloting(dto.getNoOfSloting());
		existing.setWashingHours(dto.getWashingHours());
		existing.setCity(dto.getCity());
		existing.setArea(dto.getArea());
		existing.setBorewellType(dto.getBorewellType());
		existing.setReqDrillingDepth(dto.getReqDrillingDepth());
		existing.setReq10MasterCasingPipe(dto.getReq10MasterCasingPipe());
		existing.setReq12MasterCasingPipe(dto.getReq12MasterCasingPipe());
		existing.setReq14MasterCasingPipe(dto.getReq14MasterCasingPipe());
		existing.setReqModPowder(dto.getReqModPowder());
		existing.setReqGravel(dto.getReqGravel());
		existing.setReqCasingCone(dto.getReqCasingCone());

		AreaWiseItemRequirement updated = repository.save(existing);
		return modelMapper.map(updated, AreaWiseItemRequirementDTO.class);
	}

	@Override
	public void deleteRequirement(Long id) {
		repository.deleteById(id);
	}

	@Override
	public AreaWiseItemRequirementDTO getRequirementItemDetails(String cityId, String locationAreaId,
			String borewellTypeId, String drillingSize) {

		AreaWiseItemRequirement entity = repository.getRequirementItemDetails(Long.parseLong(cityId),
				Long.parseLong(locationAreaId), Long.parseLong(borewellTypeId), drillingSize);

		if (entity == null) {
			return null;
		}

		AreaWiseItemRequirementDTO dto = new AreaWiseItemRequirementDTO();
		dto.setRequirementId(entity.getRequirementId());
		dto.setReqCasingPipe(entity.getReqCasingPipe());
		dto.setReqMasterCasingPipe(entity.getReqMasterCasingPipe());
		dto.setReq10MasterCasingPipe(entity.getReq10MasterCasingPipe());
		dto.setReq12MasterCasingPipe(entity.getReq12MasterCasingPipe());
		dto.setReq14MasterCasingPipe(entity.getReq14MasterCasingPipe());
		dto.setNoOfSloting(entity.getNoOfSloting());
		dto.setWashingHours(entity.getWashingHours());
		dto.setReqModPowder(entity.getReqModPowder());
		dto.setReqGravel(entity.getReqGravel());
		dto.setReqCasingCone(entity.getReqCasingCone());
		dto.setReqDrillingDepth(entity.getReqDrillingDepth());
		dto.setReqDrillingDia(entity.getReqDrillingDia());

		return dto;
	}

}

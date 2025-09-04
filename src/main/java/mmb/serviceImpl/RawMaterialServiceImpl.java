package mmb.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mmb.dto.RawMaterialDTO;
import mmb.model.RawMaterial;
import mmb.repository.RawMaterialRepo;
import mmb.service.RawMaterialService;

@Service
public class RawMaterialServiceImpl implements RawMaterialService {
	@Autowired
	private RawMaterialRepo rawMaterialRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public RawMaterialDTO saveMaterial(RawMaterialDTO rawMaterialDTO) {
		RawMaterial rawMaterial = modelMapper.map(rawMaterialDTO, RawMaterial.class);
		RawMaterial saved = rawMaterialRepository.save(rawMaterial);
		return modelMapper.map(saved, RawMaterialDTO.class);
	}

	@Override
	public RawMaterialDTO updateMaterial(int id, RawMaterialDTO rawMaterialDTO) {
		Optional<RawMaterial> optional = rawMaterialRepository.findById(id);
		if (optional.isPresent()) {
			RawMaterial rawMaterial = optional.get();
			rawMaterial.setMaterialName(rawMaterialDTO.getMaterialName());
			rawMaterial.setMaterialPrice(rawMaterialDTO.getMaterialPrice());
			rawMaterial.setMaterialType(rawMaterialDTO.getMaterialType());
			rawMaterial.setMatrialSize(rawMaterialDTO.getMatrialSize());
			RawMaterial updated = rawMaterialRepository.save(rawMaterial);
			return modelMapper.map(updated, RawMaterialDTO.class);
		}
		return null;
	}

	@Override
	public void deleteMaterial(int id) {
		rawMaterialRepository.deleteById(id);
	}

	@Override
	public RawMaterialDTO getMaterialById(int id) {
		RawMaterial rawMaterial = rawMaterialRepository.findById(id).orElse(null);
		return rawMaterial != null ? modelMapper.map(rawMaterial, RawMaterialDTO.class) : null;
	}

	@Override
	public List<RawMaterialDTO> getAllMaterials() {
		return rawMaterialRepository.findAll().stream().map(material -> modelMapper.map(material, RawMaterialDTO.class))
				.collect(Collectors.toList());
	}

}

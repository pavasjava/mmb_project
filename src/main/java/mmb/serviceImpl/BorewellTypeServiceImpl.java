package mmb.serviceImpl;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import mmb.dto.BorewellTypeDTO;
import mmb.model.BorewellType;
import mmb.repository.BorewellTypeRepository;
import mmb.service.BorewellTypeService;

@Service
public class BorewellTypeServiceImpl implements BorewellTypeService{
	
	private final BorewellTypeRepository borewellTypeRepo;

    public BorewellTypeServiceImpl(BorewellTypeRepository borewellTypeRepo) {
        this.borewellTypeRepo = borewellTypeRepo;
    }
    
    @Override
    public BorewellTypeDTO saveBorewellType(BorewellTypeDTO dto) {
        BorewellType type = new BorewellType();

        if (dto.getBorewelTypeid() != null) {
            type = borewellTypeRepo.findById(dto.getBorewelTypeid())
                    .orElse(new BorewellType());
        }

        type.setName(dto.getName());
        type.setDescription(dto.getDescription());

        try {
            if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
                type.setImageData(dto.getImageFile().getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        BorewellType saved = borewellTypeRepo.save(type);

        // Convert to Base64 for display
        String base64Image = saved.getImageData() != null
                ? Base64.getEncoder().encodeToString(saved.getImageData())
                : null;

        return new BorewellTypeDTO(
                saved.getBorewelTypeid(),
                saved.getName(),
                saved.getDescription(),
                null, // no file in DTO when returning
                base64Image
        );
    }

    @Override
    public List<BorewellTypeDTO> getAllTypes() {
        return borewellTypeRepo.findAll().stream().map(type -> {
            String base64Image = type.getImageData() != null
                    ? Base64.getEncoder().encodeToString(type.getImageData())
                    : null;
            return new BorewellTypeDTO(
                    type.getBorewelTypeid(),
                    type.getName(),
                    type.getDescription(),
                    null,
                    base64Image
            );
        }).collect(Collectors.toList());
    }
    
    @Override
    public BorewellTypeDTO getById(Long id) {
        BorewellType type = borewellTypeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Borewell type not found"));
        String base64Image = type.getImageData() != null
                ? Base64.getEncoder().encodeToString(type.getImageData())
                : null;
        return new BorewellTypeDTO(
                type.getBorewelTypeid(),
                type.getName(),
                type.getDescription(),
                null,
                null,
                base64Image
        );
    }

    @Override
    public void deleteById(Long id) {
        borewellTypeRepo.deleteById(id);
    }

}

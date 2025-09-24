package mmb.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorewellTypeDTO {
	private Long borewelTypeid;
	private String name;
	private String description;
	private String imageUrl;
	private MultipartFile imageFile;
	private String base64Image;

	public BorewellTypeDTO(Long borewelTypeid, String name, String description, MultipartFile imageFile,
			String base64Image) {
		this.borewelTypeid = borewelTypeid;
		this.name = name;
		this.description = description;
		this.imageFile = imageFile;
		this.base64Image = base64Image;
	}
}

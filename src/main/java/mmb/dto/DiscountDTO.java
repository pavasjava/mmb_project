package mmb.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountDTO {
	
	private Long discountId;
    private String discountType;
    private String discount;

}

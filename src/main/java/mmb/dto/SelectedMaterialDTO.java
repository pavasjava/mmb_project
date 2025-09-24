package mmb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectedMaterialDTO {

	private Integer materialId;
	private String materialType;
	private String companyName;
	private Integer quantity;

	public SelectedMaterialDTO() {
	}

	public SelectedMaterialDTO(Integer materialId, String materialType, String companyName, Integer quantity) {
		this.materialId = materialId;
		this.materialType = materialType;
		this.companyName = companyName;
		this.quantity = quantity;
	}

	public Integer getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Integer materialId) {
		this.materialId = materialId;
	}

	public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}

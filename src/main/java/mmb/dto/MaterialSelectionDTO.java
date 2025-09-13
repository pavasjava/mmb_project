package mmb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialSelectionDTO {
    private Integer materialTypeId;
    private Integer companyId;
	public Integer getMaterialTypeId() {
		return materialTypeId;
	}
	public void setMaterialTypeId(Integer materialTypeId) {
		this.materialTypeId = materialTypeId;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
}

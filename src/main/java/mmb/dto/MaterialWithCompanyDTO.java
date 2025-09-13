package mmb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialWithCompanyDTO {
	
	private String materialWithCmpName;
	private Integer materialTypeCompanyId; 
	
	public MaterialWithCompanyDTO(String materialWithCmpName, Integer materialTypeCompanyId /* , ... */) {
		this.materialWithCmpName = materialWithCmpName;
		this.materialTypeCompanyId = materialTypeCompanyId;
	}


	public String getMaterialWithCmpName() {
		return materialWithCmpName;
	}

	public void setMaterialWithCmpName(String materialWithCmpName) {
		this.materialWithCmpName = materialWithCmpName;
	}

	public Integer getMaterialTypeCompanyId() {
		return materialTypeCompanyId;
	}

	public void setMaterialTypeCompanyId(Integer materialTypeCompanyId) {
		this.materialTypeCompanyId = materialTypeCompanyId;
	}

}

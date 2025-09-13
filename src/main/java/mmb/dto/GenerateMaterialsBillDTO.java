package mmb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateMaterialsBillDTO {
    private Integer materialId;
    private Integer totalUnit;
    private Double totalAmount;
	public Integer getMaterialId() {
		return materialId;
	}
	public void setMaterialId(Integer materialId) {
		this.materialId = materialId;
	}
	public Integer getTotalUnit() {
		return totalUnit;
	}
	public void setTotalUnit(Integer totalUnit) {
		this.totalUnit = totalUnit;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
}

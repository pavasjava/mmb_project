package mmb.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawMaterialDTO {

	private Integer materialId;
	private String materialType;
	private String materialName;
	private String companyName;
	private Double materialPrice;
	private String materialSize;
	private String quality;
	private String quantity;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "quotation_id")
//    private Quotation quotation;
	
	public Integer getMaterialId() {
		return materialId;
	}
	public void setMaterialId(Integer materialId) {
		this.materialId = materialId;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public Double getMaterialPrice() {
		return materialPrice;
	}
	public void setMaterialPrice(Double materialPrice) {
		this.materialPrice = materialPrice;
	}
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	public String getMaterialSize() {
		return materialSize;
	}
	public void setMaterialSize(String materialSize) {
		this.materialSize = materialSize;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
//	public Quotation getQuotation() {
//		return quotation;
//	}
//	public void setQuotation(Quotation quotation) {
//		this.quotation = quotation;
//	}
}

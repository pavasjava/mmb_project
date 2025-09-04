package mmb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="raw_material")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawMaterial {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "material_id")
	private Integer materialId;
	@Column(name = "material_type")
	private String materialType;
	@Column(name = "material_name")
	private String materialName;
	@Column(name = "company_name")
	private String companyName;
	@Column(name = "quality")
	private String Quality;
	@Column(name = "material_price")
	private Double materialPrice;
	@Column(name = "material_size")
	private String matrialSize;
	@Column(name = "quantity")
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
	public String getMatrialSize() {
		return matrialSize;
	}
	public void setMatrialSize(String matrialSize) {
		this.matrialSize = matrialSize;
	}
	public String getQuality() {
		return Quality;
	}
	public void setQuality(String quality) {
		Quality = quality;
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

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
//	@Column(name = "material_type")
//	private String materialType;
//	@Column(name = "material_name")
//	private String materialName;
//	@Column(name = "company_name")
//	private String companyName;
	@Column(name = "quality")
	private String Quality;
	@Column(name = "material_price")
	private Double materialPrice;
	@Column(name = "material_size")
	private String matrialSize;
	@Column(name = "quantity")
	private String quantity;
	@ManyToOne
    @JoinColumn(name = "material_type_id")
    private MaterialType materialType;

	// Relation with MaterialCompanyName
    @ManyToOne
    @JoinColumn(name = "company_id")
    private MaterialCompanyName companyName;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "quotation_id")
//    private Quotation quotation;
	
	public Integer getMaterialId() {
		return materialId;
	}
	public void setMaterialId(Integer materialId) {
		this.materialId = materialId;
	}
	
	public Double getMaterialPrice() {
		return materialPrice;
	}
	public void setMaterialPrice(Double materialPrice) {
		this.materialPrice = materialPrice;
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
	public MaterialType getMaterialType() {
		return materialType;
	}
	public MaterialCompanyName getCompanyName() {
		return companyName;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
    public void setMaterialType(MaterialType materialType) {
		this.materialType = materialType;
	}
	public void setCompanyName(MaterialCompanyName companyName) {
		this.companyName = companyName;
	}
//	public Quotation getQuotation() {
//		return quotation;
//	}
//	public void setQuotation(Quotation quotation) {
//		this.quotation = quotation;
//	}
}

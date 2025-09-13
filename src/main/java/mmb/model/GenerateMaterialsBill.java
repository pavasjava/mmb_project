package mmb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "generate_materials_bill")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateMaterialsBill {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "genrate_material_bill_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private GenerateBill generateBill;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private RawMaterial rawMaterial;

    @Column(name = "total_unit")
    private Integer totalUnit;
    
    @Column(name = "total_amount")
    private Double totalAmount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public GenerateBill getGenerateBill() {
		return generateBill;
	}

	public void setGenerateBill(GenerateBill generateBill) {
		this.generateBill = generateBill;
	}

	public RawMaterial getRawMaterial() {
		return rawMaterial;
	}

	public void setRawMaterial(RawMaterial rawMaterial) {
		this.rawMaterial = rawMaterial;
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

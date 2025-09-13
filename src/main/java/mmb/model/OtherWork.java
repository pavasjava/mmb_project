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
@Table(name="other_work")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtherWork {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "oth_work_id")
	private Integer othWorkId;
	@Column(name="oth_work_name")
	private String othWorkName;
	@Column(name="total_unit")
	private String totalUnit;
	@Column(name="oth_work_price")
	private Double price;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id")
    private GenerateBill generateBill;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotation_id")
    private Quotation quotation;
	
	public Integer getOthWorkId() {
		return othWorkId;
	}

	public void setOthWorkId(Integer othWorkId) {
		this.othWorkId = othWorkId;
	}

	public String getOthWorkName() {
		return othWorkName;
	}

	public void setOthWorkName(String othWorkName) {
		this.othWorkName = othWorkName;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Quotation getQuotation() {
		return quotation;
	}

	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	}

	public String getTotalUnit() {
		return totalUnit;
	}

	public void setTotalUnit(String totalUnit) {
		this.totalUnit = totalUnit;
	}
	
	public GenerateBill getGenerateBill() {
		return generateBill;
	}

	public void setGenerateBill(GenerateBill generateBill) {
		this.generateBill = generateBill;
	}

}

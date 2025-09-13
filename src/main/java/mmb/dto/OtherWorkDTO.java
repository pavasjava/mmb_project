package mmb.dto;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mmb.model.Quotation;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtherWorkDTO {

	private Integer othWorkId;
	private String othWorkName;
	private String totalUnit;
	private Double price;
//	private Quotation quotation;
	public OtherWorkDTO(Integer othWorkId, String othWorkName, String totalUnit, Double price) {
		super();
		this.othWorkId = othWorkId;
		this.othWorkName = othWorkName;
		this.totalUnit = totalUnit;
		this.price = price;
	}
    
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
	public String getTotalUnit() {
		return totalUnit;
	}
	public void setTotalUnit(String totalUnit) {
		this.totalUnit = totalUnit;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
//	public Quotation getQuotation() {
//		return quotation;
//	}
//	public void setQuotation(Quotation quotation) {
//		this.quotation = quotation;
//	}
}

package mmb.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Transient;

import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mmb.model.OtherWork;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuotationDTO {

	private Integer quotationId;
	private String customerName;
	private String workAddress;
	private String boringType;
	private String boringDia;
	private String priceQntDtls;
	private Double drillingPrice;
	private String transportingVehicleType;
	private Double transportingPrice;
	private Date doe;
	private List<MaterialSelectionDTO> selectedMaterials = new ArrayList<>();
	private Integer selectedMaterialTypeId;
    private Integer selectedCompanyId;
	@ElementCollection
	private List<Integer> requiredMaterialIds = new ArrayList<>();

    
	

	public Integer getSelectedMaterialTypeId() {
		return selectedMaterialTypeId;
	}

	public void setSelectedMaterialTypeId(Integer selectedMaterialTypeId) {
		this.selectedMaterialTypeId = selectedMaterialTypeId;
	}

	public Integer getSelectedCompanyId() {
		return selectedCompanyId;
	}

	public void setSelectedCompanyId(Integer selectedCompanyId) {
		this.selectedCompanyId = selectedCompanyId;
	}

	public List<MaterialSelectionDTO> getSelectedMaterials() {
		return selectedMaterials;
	}

	public void setSelectedMaterials(List<MaterialSelectionDTO> selectedMaterials) {
		this.selectedMaterials = selectedMaterials;
	}

//    @Transient
//    private Integer requiredMaterialId;

	public Integer getQuotationId() {
		return quotationId;
	}

	public void setQuotationId(Integer quotationId) {
		this.quotationId = quotationId;
	}

	public String getWorkAddress() {
		return workAddress;
	}

	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}

	public String getBoringType() {
		return boringType;
	}

	public void setBoringType(String boringType) {
		this.boringType = boringType;
	}

	public String getBoringDia() {
		return boringDia;
	}

	public void setBoringDia(String boringDia) {
		this.boringDia = boringDia;
	}
	
	public String getPriceQntDtls() {
		return priceQntDtls;
	}

	public void setPriceQntDtls(String priceQntDtls) {
		this.priceQntDtls = priceQntDtls;
	}

	public Double getDrillingPrice() {
		return drillingPrice;
	}

	public void setDrillingPrice(Double drillingPrice) {
		this.drillingPrice = drillingPrice;
	}

	public String getTransportingVehicleType() {
		return transportingVehicleType;
	}

	public void setTransportingVehicleType(String transportingVehicleType) {
		this.transportingVehicleType = transportingVehicleType;
	}

	public Double getTransportingPrice() {
		return transportingPrice;
	}

	public void setTransportingPrice(Double transportingPrice) {
		this.transportingPrice = transportingPrice;
	}

	public Date getDoe() {
		return doe;
	}

	public void setDoe(Date doe) {
		this.doe = doe;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public List<Integer> getRequiredMaterialIds() {
		return requiredMaterialIds;
	}

	public void setRequiredMaterialIds(List<Integer> requiredMaterialIds) {
		this.requiredMaterialIds = requiredMaterialIds;
	}
//	public Integer getRequiredMaterialId() {
//		return requiredMaterialId;
//	}
//	public void setRequiredMaterialId(Integer requiredMaterialId) {
//		this.requiredMaterialId = requiredMaterialId;
//	}

}

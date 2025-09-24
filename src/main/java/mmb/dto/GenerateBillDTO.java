package mmb.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mmb.model.GenerateMaterialsBill;
import mmb.model.OtherWork;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateBillDTO {
	
	private Integer billId;
    private String customerName;
    private String workAddress;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate workDate;

    private String boringType;
    private String boringDia;
	private String priceQntDtls;
    private Double drillingPrice;
    private String transportingVehicleType;
    private Double transportingPrice;
    private Date doe;

    private Integer selectedMaterialTypeId;
    private Integer selectedCompanyId;

    private String mobileNo;

    @ElementCollection
    private List<Integer> requiredMaterialIds = new ArrayList<>();

    @ElementCollection
    private List<Integer> requiredMaterialQuantities = new ArrayList<>();
    @ElementCollection
    private List<String> requiredMaterialwithCompanyName = new ArrayList<>();

	private List<OtherWorkDTO> otherWorks = new ArrayList<>();
    
    private Integer totalDrilling;
	private Double totalAdvance;
	private Double totalDrillingAmt;
	
	private Double totalMatrialAmt;
	private Double totalOthWorkAmt;
	
	private List<SelectedMaterialDTO> selectedMaterials;
	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getWorkAddress() {
		return workAddress;
	}

	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}

	public LocalDate getWorkDate() {
		return workDate;
	}

	public void setWorkDate(LocalDate workDate) {
		this.workDate = workDate;
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

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public List<Integer> getRequiredMaterialIds() {
		return requiredMaterialIds;
	}

	public void setRequiredMaterialIds(List<Integer> requiredMaterialIds) {
		this.requiredMaterialIds = requiredMaterialIds;
	}

	public List<Integer> getRequiredMaterialQuantities() {
		return requiredMaterialQuantities;
	}

	public void setRequiredMaterialQuantities(List<Integer> requiredMaterialQuantities) {
		this.requiredMaterialQuantities = requiredMaterialQuantities;
	}

	public List<OtherWorkDTO> getOtherWorks() {
		return otherWorks;
	}

	public void setOtherWorks(List<OtherWorkDTO> otherWorks) {
		this.otherWorks = otherWorks;
	}


	public Integer getTotalDrilling() {
		return totalDrilling;
	}

	public void setTotalDrilling(Integer totalDrilling) {
		this.totalDrilling = totalDrilling;
	}


	public Double getTotalAdvance() {
		return totalAdvance;
	}


	public void setTotalAdvance(Double totalAdvance) {
		this.totalAdvance = totalAdvance;
	}

	public Double getTotalDrillingAmt() {
		return totalDrillingAmt;
	}

	public void setTotalDrillingAmt(Double totalDrillingAmt) {
		this.totalDrillingAmt = totalDrillingAmt;
	}

	public Double getTotalMatrialAmt() {
		return totalMatrialAmt;
	}

	public void setTotalMatrialAmt(Double totalMatrialAmt) {
		this.totalMatrialAmt = totalMatrialAmt;
	}

	public Double getTotalOthWorkAmt() {
		return totalOthWorkAmt;
	}

	public void setTotalOthWorkAmt(Double totalOthWorkAmt) {
		this.totalOthWorkAmt = totalOthWorkAmt;
	}
	
    public List<String> getRequiredMaterialwithCompanyName() {
		return requiredMaterialwithCompanyName;
	}

	public void setRequiredMaterialwithCompanyName(List<String> requiredMaterialwithCompanyName) {
		this.requiredMaterialwithCompanyName = requiredMaterialwithCompanyName;
	}

	public List<SelectedMaterialDTO> getSelectedMaterials() {
		return selectedMaterials;
	}

	public void setSelectedMaterials(List<SelectedMaterialDTO> selectedMaterials) {
		this.selectedMaterials = selectedMaterials;
	}	
}

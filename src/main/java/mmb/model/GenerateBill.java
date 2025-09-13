package mmb.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="generate_bill")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateBill {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bill_id")
	private Integer billId;
	@Column(name = "work_address")
	private String workAddress;
	@Column(name = "work_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate workDate;
	@Column(name = "bore_type")
	private String boringType;
	@Column(name = "bore_dia")
	private String boringDia;
	@Column(name = "price_qnt_dtls")
	private String priceQntDtls;
	@Column(name = "drilling_price")
	private Double drillingPrice;
	@Column(name = "trans_vehicle_type")
	private String transportingVehicleType;
	@Column(name = "transport_price")
	private Double transportingPrice;
	@Temporal(TemporalType.TIMESTAMP)
    private Date doe;
	@Column(name = "mobile_No")
	private String mobileNo;
	@Column(name = "total_drilling")
	private Integer totalDrilling;
	@Column(name = "total_advance")
	private Double totalAdvance;
	@Column(name = "total_drilling_amt")
	private Double totalDrillingAmt;
	@Column(name = "total_material_amt")
	private Double totalMatrialAmt;
	@Column(name = "total_oth_work_amt")
	private Double totalOthWorkAmt;

	@PrePersist
    protected void onCreate() {
        this.doe = new Date(); 
    }
    
	@Column(name = "customerName")
	private String customerName;
	
	
//	@ManyToMany
//	@JoinTable(
//	    name = "generate_materials_bill",
//	    joinColumns = @JoinColumn(name = "bill_id"),
//	    inverseJoinColumns = @JoinColumn(name = "material_id")
//	)
//	private List<RawMaterial> requiredMaterials = new ArrayList<>();
	
	@OneToMany(mappedBy = "generateBill", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OtherWork> otherWorks = new ArrayList<>();

	@OneToMany(mappedBy = "generateBill", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<GenerateMaterialsBill> materialsBill = new ArrayList<>();
	
	public List<GenerateMaterialsBill> getMaterialsBill() {
		return materialsBill;
	}


	public void setMaterialsBill(List<GenerateMaterialsBill> materialsBill) {
		this.materialsBill = materialsBill;
	}


	public Integer getBillId() {
		return billId;
	}


	public void setBillId(Integer billId) {
		this.billId = billId;
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


//	public List<RawMaterial> getRequiredMaterials() {
//		return requiredMaterials;
//	}
//
//
//	public void setRequiredMaterials(List<RawMaterial> requiredMaterials) {
//		this.requiredMaterials = requiredMaterials;
//	}
	

	public LocalDate getWorkDate() {
		return workDate;
	}


	public void setWorkDate(LocalDate workDate) {
		this.workDate = workDate;
	}
	
    public String getMobileNo() {
		return mobileNo;
	}


	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public List<OtherWork> getOtherWorks() {
		return otherWorks;
	}

	public void setOtherWorks(List<OtherWork> otherWorks) {
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
		
}

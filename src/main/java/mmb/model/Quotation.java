package mmb.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
@Table(name = "quotation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Quotation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "quotation_id")
	private Integer quotationId;
	@Column(name = "work_address")
	private String workAddress;
	@Column(name = "bore_type")
	private String boringType;
	@Column(name = "bore_dia")
	private String boringDia;
	@Column(name = "drilling_price")
	private Double drillingPrice;
	@Column(name = "trans_vehicle_type")
	private String transportingVehicleType;
	@Column(name = "transport_price")
	private Double transportingPrice;
	@Temporal(TemporalType.TIMESTAMP)
    private Date doe;

    @PrePersist
    protected void onCreate() {
        this.doe = new Date(); 
    }
    
	@Column(name = "customerName")
	private String customerName;
	
	
	@ManyToMany
	@JoinTable(
	    name = "quotation_materials",
	    joinColumns = @JoinColumn(name = "quotation_id"),
	    inverseJoinColumns = @JoinColumn(name = "material_id")
	)
	private List<RawMaterial> requiredMaterials = new ArrayList<>();
	
//	@ManyToOne
//	@JoinColumn(name = "material_id")
//	private RawMaterial requiredMaterial;
	
//	@Transient
//    private Integer requiredMaterialId;

//	@OneToMany(mappedBy = "quotation", cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<RawMaterial> rawMaterials = new ArrayList<>();
	
	

//	public Integer getRequiredMaterialId() {
//		return requiredMaterialId;
//	}
//
//	public RawMaterial getRequiredMaterial() {
//		return requiredMaterial;
//	}
//
//	public void setRequiredMaterial(RawMaterial requiredMaterial) {
//		this.requiredMaterial = requiredMaterial;
//	}
//
//	public void setRequiredMaterialId(Integer requiredMaterialId) {
//		this.requiredMaterialId = requiredMaterialId;
//	}

	public List<RawMaterial> getRequiredMaterials() {
		return requiredMaterials;
	}

	public void setRequiredMaterials(List<RawMaterial> requiredMaterials) {
		this.requiredMaterials = requiredMaterials;
	}

	@OneToMany(mappedBy = "quotation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OtherWork> otherWorks = new ArrayList<>();

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

//	public List<RawMaterial> getRawMaterials() {
//		return rawMaterials;
//	}
//
//	public void setRawMaterials(List<RawMaterial> rawMaterials) {
//		this.rawMaterials = rawMaterials;
//	}

	public List<OtherWork> getOtherWorks() {
		return otherWorks;
	}

	public void setOtherWorks(List<OtherWork> otherWorks) {
		this.otherWorks = otherWorks;
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

}

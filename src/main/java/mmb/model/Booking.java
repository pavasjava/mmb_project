package mmb.model;

import java.time.LocalDate;

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
@Table(name="booking")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking { 
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "price")
    private Double price;

    @Column(name = "booking_date")
    private LocalDate bookingDate;

    @Column(name = "status")
    private String status;
    
	@Column(name = "drilling_size")
	private String drillingSize;
	
	@Column(name = "other_size")
    private String otherSize;
	
	@Column(name = "casing_pipe_quality")
    private String pipeQuality;
	
	@Column(name = "casing_price")
	private Double casingPrice;
	
//	@Column(name = "ms_pipe_quality")
//    private String msPipeQuality;
//	
//	@Column(name = "ms_pipe_price")
//    private String msPipePrice;

	// ✅ Add relationship with City
    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    // ✅ Add relationship with WorkLocationArea
    @ManyToOne
    @JoinColumn(name = "location_area_id", nullable = false)
    private WorkLocationArea workLocationArea;
    
    @ManyToOne
    @JoinColumn(name = "borewell_type_id", nullable = true) // make nullable for now
    private BorewellType borewellType;
    
    @ManyToOne
    @JoinColumn(name = "casing_pipe_company_id", nullable = true)
    private MaterialCompanyName companyName;
    
//    @Column(name = "tot_Drilling_unit")    
//    private Integer totalDrillingUnit;
//    @Column(name = "tot_Drilling_price")
//    private Double totalDrillingPrice;
//    private Integer totalUnitCasing;
//    @Column(name = "tot_casing_price")
//    private Double totalCasingPrice;
//    @Column(name = "tot_mc_unit")
//    private Integer totalUnitMasterCasing;
//    @Column(name = "tot_mc_unit_price")
//    private Double masterCasingPricePerUnit;
//    @Column(name = "tot_mc_price")
//    private Double totalMasterCasingPrice;
//    @Column(name = "casing_transporting")
//    private Double casingTransporting;
//    @Column(name = "tot_sloting_unit")
//    private Integer totalUnitSloting;
//    @Column(name = "sloting_price_unit")
//    private Double casingSlotingPerUnit;
//    @Column(name = "tot_sloting_price")
//    private Double totalSlotingPrice;
//    @Column(name = "tot_unit_mod_powder")
//    private Integer totalUnitModPowder;
//    @Column(name = "mp_price_unit")
//    private Double modPowderPerUnit;
//    @Column(name = "tot_mp_price_unit")
//    private Double totalModPowderPrice;
//    @Column(name = "tot_gravel_unit")
//    private Integer totalUnitGravel;
////    @Column(name = "tot_gravel_price")
////    private Double totalGravelPrice;
//    @Column(name = "tot_washing_unit")
//    private Integer totalWashingUnit;
//    @Column(name = "washing_price_unit")
//    private Double washingPricePerUnit;
//    @Column(name = "tot_washing_price")
//    private Double totalWashingPrice;
//    @Column(name = "other_item_dtls")
//    private String otherItemDetails;
//    @Column(name = "other_item_price")
//    private Double otherItemPrice;
////    @Column(name = "gravel_price")
////    private String gravelPrice;
//    @Column(name = "gravel_unit_price")
//    private Double totalUnitGravelPrice;
//    
//    @Column(name = "tot_unit_mc10")
//    private Integer totalUnitMasterCasing10;
//    @Column(name = "mc10_price_unit")
//    private Double masterCasing10PricePerUnit;
//    @Column(name = "tot_mc10_price")
//    private Double totalMaster10CasingPrice;
//    @Column(name = "tot_unit_mc12")
//    private Integer totalUnitMasterCasing12;
//    @Column(name = "mc12_price_unit")
//    private Double masterCasing12PricePerUnit;
//    @Column(name = "tot_mc12_price")
//    private Double totalMaster12CasingPrice;
//    @Column(name = "tot_mc14_unit")
//    private Integer totalUnitMasterCasing14;
//    @Column(name = "mc14_price_unit")
//    private Double masterCasing14PricePerUnit;
//    @Column(name = "tot_mc14_price")
//    private Double totalMasterCasing14Price;
    
}

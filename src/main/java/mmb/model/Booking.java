package mmb.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	
	@Column(name = "last_updated_date")
	private LocalDateTime lastUpdatedDate;

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

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;
    
    @Column(name = "Drilling_unit")    
    private Integer totalDrillingUnit;
    @Column(name = "casing_unit")  
    private Integer totalUnitCasing;
    @Column(name = "master_casing_unit")
    private Integer totalUnitMasterCasing;
    @Column(name = "mc_unit_price")
    private Double masterCasingPricePerUnit;
    @Column(name = "casing_transporting_price")
    private Double casingTransportingPrice;
    @Column(name = "casing_sloting_unit")
    private Integer totalUnitSloting;
    @Column(name = "sloting_price_unit")
    private Double casingSlotingPricePerUnit;
    @Column(name = "mod_powder_unit")
    private Integer totalUnitModPowder;
    @Column(name = "mp_price_unit")
    private Double modPowderPricePerUnit;
    @Column(name = "gravel_unit")
    private Integer totalUnitGravel;
    @Column(name = "gravel_unit_price")
    private Double gravelPricePerUnit;
    @Column(name = "tot_washing_unit")
    private Integer totalWashingUnit;
    @Column(name = "washing_price_unit")
    private Double washingPricePerUnit;
    @Column(name = "tot_unit_mc10")
    private Integer totalUnitMC10;
    @Column(name = "mc10_price_unit")
    private Double mc10PricePerUnit;
    @Column(name = "tot_unit_mc12")
    private Integer totalUnitMC12;
    @Column(name = "mc12_price_unit")
    private Double mc12PricePerUnit;
    @Column(name = "tot_mc14_unit")
    private Integer totalUnitMC14;
    @Column(name = "mc14_price_unit")
    private Double mc14PricePerUnit;
    @Column(name = "cgst_percent")
    private Integer cgstPercent;
    @Column(name = "sgst_percent")
    private Integer sgstPercent;
    
    @Column(name = "total_unit_2_5kg")
    private Integer totalUnit2_5kg;
    
    @Column(name = "price_per_unit_2_5kg")
    private Double pricePerUnit2_5kg;
    
    @Column(name = "total_2_5kg_price")
    private Double total2_5kgPrice;
    
    @Column(name = "total_unit_6kg")
    private Integer totalUnit6kg;
    
    @Column(name = "price_per_unit_6kg")
    private Double pricePerUnit6kg;
    
    @Column(name = "total_6kg_price")
    private Double total6kgPrice;
   
}

package mmb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@Table(name="item_requirement")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AreaWiseItemRequirement {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "requirement_id")
    private Long requirementId;
	
	@Column(name = "req_casing_pipe")
    private Integer reqCasingPipe;

    @Column(name = "req_master_casing_pipe")
    private Integer reqMasterCasingPipe;
    
    @Column(name = "req_10_master_casing_pipe")
    private Integer req10MasterCasingPipe;
    
    @Column(name = "req_12_master_casing_pipe")
    private Integer req12MasterCasingPipe;
    
    @Column(name = "req_14_master_casing_pipe")
    private Integer req14MasterCasingPipe;

    @Column(name = "no_of_sloting")
    private Integer noOfSloting;

    @Column(name = "washing_hours")
    private Integer washingHours;
    
    @Column(name = "mod_powder")
    private Integer reqModPowder;
    
    @Column(name = "gravel")
    private Integer reqGravel;
    
    @Column(name = "casing_cone")
    private Integer reqCasingCone;
    
    @Column(name = "drilling_depth")
    private Integer reqDrillingDepth;
    
    @Column(name = "drilling_dia")
    private String reqDrillingDia;

    // ✅ Many requirements belong to one area
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_area_id", nullable = false)
    @JsonIgnoreProperties({"itemRequirements", "city", "hibernateLazyInitializer", "handler"})
    private WorkLocationArea area;

    // ✅ Each area requirement also belongs to a city
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    @JsonIgnoreProperties({"locationAreas", "itemRequirements", "hibernateLazyInitializer", "handler"})
    private City city;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borewell_type_id", nullable = false)
    @JsonIgnoreProperties({"itemRequirements", "drillingPriceCharts", "hibernateLazyInitializer", "handler"})
    private BorewellType borewellType;

}

package mmb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name="drilling_price", schema = "mmb")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrillingPriceChart {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "price_id")
    private Long priceId;

	@Column(name = "drilling_type")
	private String drillingType;
	@Column(name = "drilling_size")
    private String drillingSize;
	@Column(name = "other_size")
    private String otherSize;
	@Column(name = "price")
    private Double price;

	@Column(name = "status")
    private String status;
	
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
    @JsonIgnore
    private BorewellType borewellType;

}

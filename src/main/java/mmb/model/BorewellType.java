package mmb.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "borewell_type")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BorewellType {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "borewell_type_id")
    private Long borewelTypeid;
	@Column(name = "name")
    private String name;       
	@Column(name = "description")
    private String description; 
	@Column(name = "image")
    private String imageUrl;
	
	@Lob
    @Column(columnDefinition = "LONGBLOB")  
    private byte[] imageData;
	
	// ✅ One BorewellType has many DrillingPriceChart
    @OneToMany(mappedBy = "borewellType", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<DrillingPriceChart> drillingPriceCharts = new ArrayList<>();
    
    @OneToMany(mappedBy = "borewellType", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<AreaWiseItemRequirement> itemRequirements = new ArrayList<>();

}

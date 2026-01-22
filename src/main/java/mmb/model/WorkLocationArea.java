package mmb.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "work_location_area")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class WorkLocationArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_area_id")
    private Long locationAreaId;

    @Column(name = "location_area_name", nullable = false)
    private String locationAreaName;

    // Many Areas belong to one City
    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    @JsonIgnoreProperties({"locationAreas", "itemRequirements", "hibernateLazyInitializer", "handler"})
    private City city;
    
    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore  // Add this to break the circular reference
    private List<AreaWiseItemRequirement> itemRequirements = new ArrayList<>();
}

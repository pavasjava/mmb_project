package mmb.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
@Table(name = "work_location_area")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    @JsonBackReference  // Prevent infinite recursion in JSON
    private City city;
}

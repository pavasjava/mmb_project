package mmb.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="material_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "material_type_id")
	private Integer materialTypeId;
	@Column(name = "material_name")
	private String materialName;
	
    @ManyToMany
    @JoinTable(
        name = "material_type_company", 
        joinColumns = @JoinColumn(name = "material_type_id"), 
        inverseJoinColumns = @JoinColumn(name = "company_id") 
    )
    private Set<MaterialCompanyName> companies = new HashSet<>();

	public Integer getMaterialTypeId() {
		return materialTypeId;
	}

	public void setMaterialTypeId(Integer materialTypeId) {
		this.materialTypeId = materialTypeId;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public Set<MaterialCompanyName> getCompanies() {
		return companies;
	}

	public void setCompanies(Set<MaterialCompanyName> companies) {
		this.companies = companies;
	}

}

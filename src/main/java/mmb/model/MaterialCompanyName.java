package mmb.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="material_company_name")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialCompanyName {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "company_id")
	private Integer companyId;
	@Column(name = "company_name")
	private String companyName;
	
	@ManyToMany(mappedBy = "companies")
    private Set<MaterialType> materialTypes = new HashSet<>();

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Set<MaterialType> getMaterialTypes() {
		return materialTypes;
	}

	public void setMaterialTypes(Set<MaterialType> materialTypes) {
		this.materialTypes = materialTypes;
	}
	

}

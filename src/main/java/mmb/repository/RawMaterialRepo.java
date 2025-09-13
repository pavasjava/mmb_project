package mmb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mmb.model.RawMaterial;

public interface RawMaterialRepo extends JpaRepository<RawMaterial, Integer> {
	
	Optional<RawMaterial> findByMaterialType_MaterialTypeIdAndCompanyName_CompanyId(
            Integer materialTypeId, Integer companyId
    );


}

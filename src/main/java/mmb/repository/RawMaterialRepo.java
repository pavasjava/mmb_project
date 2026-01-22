package mmb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mmb.dto.RawMaterialDTO;
import mmb.model.RawMaterial;

public interface RawMaterialRepo extends JpaRepository<RawMaterial, Integer> {

	Optional<RawMaterial> findByMaterialType_MaterialTypeIdAndCompanyName_CompanyId(Integer materialTypeId,
			Integer companyId);

	@Query("SELECT rm.materialPrice FROM RawMaterial rm LEFT JOIN rm.companyName mcn " 
	        + "LEFT JOIN rm.materialType mt WHERE mcn.companyId = :companyId " 
			+ "AND mt.materialTypeId = :materialTypeId AND rm.materialSize = :materialSize " 
	        + "AND rm.quality = :quality")
	Double findByCompanyAndTypeAndSizeAndQuality(@Param("companyId") Integer companyId,
			@Param("materialTypeId") Long materialTypeId, @Param("materialSize") String materialSize,
			@Param("quality") String quality);
	
	@Query("SELECT rm FROM RawMaterial rm "
			+ "JOIN rm.materialType mt "
			+ "WHERE mt.materialName = 'MASTER CASING' AND  rm.materialSize = :materialSize AND rm.quality = :quality")
	RawMaterial findMasterCasingDetails(@Param("materialSize") String materialSize,
			@Param("quality") String quality); 
	
	@Query("SELECT rm FROM RawMaterial rm "
			+ "JOIN rm.materialType mt "
			+ "WHERE mt.materialName = 'MASTER CASING' AND  rm.materialSize = :materialSize")
	RawMaterial findMasterCasingDetails(@Param("materialSize") String materialSize); 

	@Query("SELECT rm FROM RawMaterial rm "
			+ "JOIN rm.materialType mt "
			+ "WHERE mt.materialName = :materialName ")
	RawMaterial findMaterialDetailsByMaterialName(@Param("materialName") String materialName);
}

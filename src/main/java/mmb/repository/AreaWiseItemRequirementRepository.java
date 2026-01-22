package mmb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mmb.model.AreaWiseItemRequirement;

public interface AreaWiseItemRequirementRepository extends JpaRepository<AreaWiseItemRequirement, Long> {

	@Query(value = """
			SELECT ir.*
			FROM mmb.item_requirement ir
			WHERE ir.city_id = :cityId AND ir.location_area_id = :areaId AND ir.borewell_type_id = :borewellTypeId AND ir.drilling_dia = :borewellSize
			""", nativeQuery = true)
	AreaWiseItemRequirement getRequirementItemDetails(@Param("cityId") Long cityId, @Param("areaId") Long areaId,
			@Param("borewellTypeId") Long borewellTypeId, @Param("borewellSize") String borewellSize);

}

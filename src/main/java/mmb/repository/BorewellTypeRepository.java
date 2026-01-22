package mmb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mmb.model.BorewellType;

@Repository
public interface BorewellTypeRepository extends JpaRepository<BorewellType, Long> {
	
	@Query(value = """
	        SELECT bt.* 
	        FROM mmb.drilling_price dp
	        LEFT JOIN mmb.borewell_type bt ON dp.borewell_type_id = bt.borewell_type_id
	        LEFT JOIN mmb.work_location_area wla ON dp.location_area_id = wla.location_area_id
	        LEFT JOIN mmb.city city ON wla.city_id = city.city_id
	        WHERE dp.city_id = :cityId AND dp.location_area_id = :areaId AND dp.drilling_size = :drillingSize
	        """,
	        nativeQuery = true)
	    List<BorewellType> getBorewellTypeByAreaWise(@Param("cityId") String cityId,
	                                                 @Param("areaId") String areaId, @Param("drillingSize") String drillingSize);

}

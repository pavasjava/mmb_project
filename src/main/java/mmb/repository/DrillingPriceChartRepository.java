package mmb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mmb.model.DrillingPriceChart;

@Repository
public interface DrillingPriceChartRepository extends JpaRepository<DrillingPriceChart, Long> {

	@Query("SELECT d.price FROM DrillingPriceChart d " + "WHERE d.borewellType.borewelTypeid = :borewellTypeId "
			+ "AND d.city.cityId = :cityId " + "AND d.workLocationArea.locationAreaId = :locationAreaId "
			+ "AND TRIM(d.drillingSize) = TRIM(:drillingSize)")
	Double findPrice(@Param("borewellTypeId") Long borewellTypeId, @Param("cityId") Long cityId,
			@Param("locationAreaId") Long locationAreaId, @Param("drillingSize") String drillingSize);

	@Query("SELECT dp FROM DrillingPriceChart dp " + "LEFT JOIN dp.borewellType bt " + "LEFT JOIN dp.city city "
			+ "LEFT JOIN dp.workLocationArea wla " + "WHERE dp.drillingSize = :drillingSize "
			+ "AND bt.borewelTypeid = :borewelTypeid " + "AND city.cityId = :cityId "
			+ "AND wla.locationAreaId = :locationAreaId")
	DrillingPriceChart findDrillingPrice(@Param("drillingSize") String drillingSize,
			@Param("borewelTypeid") Long borewelTypeid, @Param("cityId") Long cityId,
			@Param("locationAreaId") Long locationAreaId);

}

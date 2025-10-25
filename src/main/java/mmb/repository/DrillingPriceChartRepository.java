package mmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mmb.model.DrillingPriceChart;

@Repository
public interface DrillingPriceChartRepository extends JpaRepository<DrillingPriceChart, Long> {

}

package mmb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mmb.model.WorkLocationArea;

@Repository
public interface WorkLocationAreaRepository extends JpaRepository<WorkLocationArea, Long> {
    List<WorkLocationArea> findByCity_CityId(Long cityId);
}

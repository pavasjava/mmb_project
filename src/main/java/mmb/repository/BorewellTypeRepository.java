package mmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mmb.model.Booking;
import mmb.model.BorewellType;

@Repository
public interface BorewellTypeRepository extends JpaRepository<BorewellType, Long> {

}

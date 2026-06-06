package mmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mmb.model.AreaWiseItemRequirement;
import mmb.model.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

	@Query(value = """
			SELECT dis.*
			FROM mmb.discount dis
			WHERE dis.discount_type = :discountType""", nativeQuery = true)
	Discount getDiscount(@Param("discountType") String discountType);
	
}

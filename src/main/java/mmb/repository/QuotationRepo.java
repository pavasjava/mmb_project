package mmb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mmb.model.Quotation;

public interface QuotationRepo extends JpaRepository<Quotation, Integer> {

	@Query("SELECT q FROM Quotation q LEFT JOIN FETCH q.requiredMaterials WHERE q.quotationId = :id")
    Optional<Quotation> findByIdWithMaterials(@Param("id") Integer id);
}

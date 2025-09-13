package mmb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mmb.dto.MaterialWithCompanyProjection;
import mmb.model.GenerateBill;

public interface GenerateBillRepo extends JpaRepository<GenerateBill, Integer>{
	
//	@Query("SELECT gb FROM GenerateBill gb LEFT JOIN FETCH gb.requiredMaterials WHERE gb.billId = :id")
//	Optional<GenerateBill> findByIdWithMaterials(@Param("id") Integer id);
	
	@Query("SELECT gb FROM GenerateBill gb " +
		       "LEFT JOIN FETCH gb.materialsBill mb " +
		       "LEFT JOIN FETCH mb.rawMaterial " +
		       "WHERE gb.billId = :id")
		Optional<GenerateBill> findByIdWithMaterials(@Param("id") Integer id);

	@Query(value = "SELECT CONCAT(mt.material_name, ' (', mcn.company_name, ')') AS materialWithCmpName,rm.material_id FROM mmb.raw_material rm "
			+ "left outer join mmb.material_type mt on rm.material_type_id = mt.material_type_id "
			+ "left outer join mmb.material_company_name mcn on rm.company_id = mcn.company_id order by materialWithCmpName", nativeQuery = true)
	List<MaterialWithCompanyProjection> findAllMaterialsDtls();
	
	@Query("SELECT g FROM GenerateBill g ORDER BY g.doe DESC")
    List<GenerateBill> findAllByOrderByDoeDesc();

}

package mmb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mmb.model.GenerateBill;
import mmb.model.GenerateMaterialsBill;
import mmb.model.RawMaterial;

public interface GenerateMaterialsBillRepository extends JpaRepository<GenerateMaterialsBill, Long> {
    
    List<GenerateMaterialsBill> findByGenerateBill(GenerateBill bill);

    List<GenerateMaterialsBill> findByRawMaterial(RawMaterial material);
}

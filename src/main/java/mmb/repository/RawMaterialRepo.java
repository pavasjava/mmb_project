package mmb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mmb.model.RawMaterial;

public interface RawMaterialRepo extends JpaRepository<RawMaterial, Integer> {

}

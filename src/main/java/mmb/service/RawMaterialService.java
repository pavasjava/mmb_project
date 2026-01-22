package mmb.service;

import java.util.List;

import mmb.dto.RawMaterialDTO;

public interface RawMaterialService {
	
	RawMaterialDTO saveMaterial(RawMaterialDTO rawMaterialDTO);
    RawMaterialDTO updateMaterial(int id, RawMaterialDTO rawMaterialDTO);
    void deleteMaterial(int id);
    RawMaterialDTO getMaterialById(int id);
    List<RawMaterialDTO> getAllMaterials();
    RawMaterialDTO findMasterCasingDetails(String materialSize, String quality);
    RawMaterialDTO findMasterCasingDetails(String materialSize);
    RawMaterialDTO findMaterialDetailsByMaterialName(String materialName);

}

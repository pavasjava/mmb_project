package mmb.service.rest;

import java.io.OutputStream;
import java.util.List;

import mmb.dto.GenerateBillDTO;
import mmb.dto.MaterialWithCompanyProjection;
import mmb.model.GenerateBill;
import mmb.model.MaterialCompanyName;
import mmb.model.MaterialType;

public interface GenerateBillRestService {
	
	List<GenerateBillDTO> getAllBills();
	GenerateBillDTO getBillById(Integer id);
    void deleteBillById(Integer id);
    public GenerateBillDTO saveOrUpdate(GenerateBillDTO dto);
    public List<MaterialWithCompanyProjection> getAllMaterials();

    public List<MaterialType> getAllMaterialTypes();

    public List<MaterialCompanyName> getAllCompanies();
    public void generateBillPdf(GenerateBillDTO bill, OutputStream out);

}

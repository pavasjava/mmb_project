package mmb.service;

import java.io.OutputStream;
import java.util.List;

import mmb.dto.GenerateBillDTO;
import mmb.dto.MaterialWithCompanyProjection;
import mmb.model.GenerateBill;
import mmb.model.MaterialCompanyName;
import mmb.model.MaterialType;

public interface GenerateBillService {

	List<GenerateBill> getAllBills();
	GenerateBillDTO getBillById(Integer id);
    void deleteBillById(Integer id);
    public GenerateBill saveOrUpdate(GenerateBillDTO dto);
    public List<MaterialWithCompanyProjection> getAllMaterials();

    public List<MaterialType> getAllMaterialTypes();

    public List<MaterialCompanyName> getAllCompanies();
    public void generateBillPdf(GenerateBillDTO bill, OutputStream out);
}

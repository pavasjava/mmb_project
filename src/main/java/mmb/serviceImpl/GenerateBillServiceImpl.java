package mmb.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import mmb.dto.GenerateBillDTO;
import mmb.dto.OtherWorkDTO;
import mmb.dto.RawMaterialDTO;
import mmb.model.GenerateBill;
import mmb.model.GenerateMaterialsBill;
import mmb.model.OtherWork;
import mmb.model.RawMaterial;
import mmb.repository.GenerateBillRepo;
import mmb.repository.OtherWorkRepo;
import mmb.repository.RawMaterialRepo;
import mmb.service.GenerateBillService;

@Service
@Transactional
public class GenerateBillServiceImpl implements GenerateBillService {

	private final GenerateBillRepo generateBillRepo;
	private final RawMaterialRepo rawMaterialRepo;
	private final OtherWorkRepo otherWorkRepo;

    @Autowired
    public GenerateBillServiceImpl(GenerateBillRepo generateBillRepository, RawMaterialRepo rawMaterialRepo, OtherWorkRepo otherWorkRepo) {
        this.generateBillRepo = generateBillRepository;
        this.rawMaterialRepo = rawMaterialRepo;
        this.otherWorkRepo = otherWorkRepo;
    }

 // Get all bills
    @Override
    public List<GenerateBill> getAllBills() {
        return generateBillRepo.findAllByOrderByDoeDesc();
    }

 // Get bill by ID
    @Override
    public GenerateBill getBillById(Integer billId) {
        return generateBillRepo.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found with id " + billId));
    }

    // Save or update bill
    @Override
    @Transactional
    public GenerateBill saveOrUpdate(GenerateBillDTO dto) {
        GenerateBill bill;

        // if edit, fetch existing
        if (dto.getBillId() != null) {
            bill = generateBillRepo.findById(dto.getBillId())
                    .orElseThrow(() -> new RuntimeException("Bill not found with id " + dto.getBillId()));
        } else {
            bill = new GenerateBill();
        }

        bill.setCustomerName(dto.getCustomerName());
        bill.setMobileNo(dto.getMobileNo());
        bill.setWorkAddress(dto.getWorkAddress());
        bill.setWorkDate(dto.getWorkDate());
        bill.setBoringType(dto.getBoringType());
        bill.setBoringDia(dto.getBoringDia());
        bill.setPriceQntDtls(dto.getPriceQntDtls());
        bill.setDrillingPrice(dto.getDrillingPrice());
        bill.setTransportingVehicleType(dto.getTransportingVehicleType());
        bill.setTransportingPrice(dto.getTransportingPrice());
        bill.setTotalDrilling(dto.getTotalDrilling());
        bill.setTotalAdvance(dto.getTotalAdvance());
        double totalDrillingAmt = dto.getTotalDrilling()*dto.getDrillingPrice();
        bill.setTotalDrillingAmt(totalDrillingAmt);
        
        double totalMaterialAmt = 0.0;
        double totalOtherWorkAmt = 0.0;
        
        List<GenerateMaterialsBill> materialsBills = new ArrayList<>();
        if (dto.getRequiredMaterialIds() != null && dto.getRequiredMaterialQuantities() != null) {
            for (int i = 0; i < dto.getRequiredMaterialIds().size(); i++) {
                Integer materialId = dto.getRequiredMaterialIds().get(i);
                Integer qty = dto.getRequiredMaterialQuantities().get(i);

                RawMaterial material = rawMaterialRepo.findById(materialId)
                        .orElseThrow(() -> new RuntimeException("Material not found with id " + materialId));
                
                System.out.println("material price -> "+ material.getMaterialPrice());
                System.out.println("Total material price -> "+ material.getMaterialPrice()*qty);

                GenerateMaterialsBill gmb = new GenerateMaterialsBill();
                gmb.setGenerateBill(bill);
                gmb.setRawMaterial(material);
                gmb.setTotalUnit(qty);
                double totalMaterialPrice = material.getMaterialPrice()*qty;
                totalMaterialAmt += totalMaterialPrice;
                gmb.setTotalAmount(totalMaterialPrice);

                materialsBills.add(gmb);
            }
        }
        bill.setMaterialsBill(materialsBills);

        List<OtherWork> works = new ArrayList<>();
        if (dto.getOtherWorks() != null) {
            for (OtherWorkDTO owDto : dto.getOtherWorks()) {
                OtherWork work = new OtherWork();
                work.setOthWorkName(owDto.getOthWorkName());
                work.setTotalUnit(owDto.getTotalUnit());
                work.setPrice(owDto.getPrice());
                double otherWorkPrice = owDto.getPrice();
                totalOtherWorkAmt += otherWorkPrice;
                work.setGenerateBill(bill);
                works.add(work);
            }
        }
        bill.setOtherWorks(works);
        bill.setTotalMatrialAmt(totalMaterialAmt);
        bill.setTotalOthWorkAmt(totalOtherWorkAmt);

        return generateBillRepo.save(bill);
    }



	@Override
	public void deleteBillById(Integer billId) {
		generateBillRepo.deleteById(billId);
	}

}

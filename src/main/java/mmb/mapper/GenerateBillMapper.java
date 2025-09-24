package mmb.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import mmb.dto.GenerateBillDTO;
import mmb.dto.OtherWorkDTO;
import mmb.model.GenerateBill;
import mmb.model.GenerateMaterialsBill;
import mmb.model.RawMaterial;

@Component
public class GenerateBillMapper {
	
	public GenerateBillDTO mapToDTO(GenerateBill bill) {
        GenerateBillDTO dto = new GenerateBillDTO();
        // Copy simple fields
        dto.setBillId(bill.getBillId());
        dto.setCustomerName(bill.getCustomerName());
        dto.setWorkAddress(bill.getWorkAddress());
        dto.setWorkDate(bill.getWorkDate());
        dto.setBoringType(bill.getBoringType());
        dto.setBoringDia(bill.getBoringDia());
        dto.setPriceQntDtls(bill.getPriceQntDtls());
        dto.setDrillingPrice(bill.getDrillingPrice());
        dto.setTransportingVehicleType(bill.getTransportingVehicleType());
        dto.setTransportingPrice(bill.getTransportingPrice());
        dto.setDoe(bill.getDoe());
        dto.setMobileNo(bill.getMobileNo());
        dto.setTotalDrilling(bill.getTotalDrilling());
        dto.setTotalAdvance(bill.getTotalAdvance());
        dto.setTotalDrillingAmt(bill.getTotalDrillingAmt());
        dto.setTotalMatrialAmt(bill.getTotalMatrialAmt());
        dto.setTotalOthWorkAmt(bill.getTotalOthWorkAmt());

        // Handle materials
        for (GenerateMaterialsBill gmb : bill.getMaterialsBill()) {
            RawMaterial raw = gmb.getRawMaterial();

            dto.getRequiredMaterialIds().add(raw.getMaterialId());
            dto.getRequiredMaterialQuantities().add(gmb.getTotalUnit());
            dto.getRequiredMaterialwithCompanyName().add(raw.getCompanyName().getCompanyName());

            // Single values (if needed)
            dto.setSelectedMaterialTypeId(raw.getMaterialType().getMaterialTypeId());
            dto.setSelectedCompanyId(raw.getCompanyName().getCompanyId());
        }
        

        // ✅ Handle other works
        if (bill.getOtherWorks() != null) {
            List<OtherWorkDTO> otherWorkDTOs = bill.getOtherWorks().stream()
                .map(ow -> new OtherWorkDTO(
                        ow.getOthWorkId(),           
                        ow.getOthWorkName(),
                        ow.getTotalUnit(),
                        ow.getPrice()
                ))
                .collect(Collectors.toList());

            dto.setOtherWorks(otherWorkDTOs);
        }

        return dto;
    }

}

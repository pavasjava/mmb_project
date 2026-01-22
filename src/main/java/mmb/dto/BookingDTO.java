package mmb.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mmb.model.BorewellType;
import mmb.model.City;
import mmb.model.MaterialCompanyName;
import mmb.model.WorkLocationArea;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
	
	private Long bookingId;
    private String customerName;
    private String phoneNumber;
    private Double price;
    private LocalDate bookingDate;
    private String status;
    private String drillingSize;
    private String otherSize;

    private City city;                // ✅ Matches booking.getCity()
    private WorkLocationArea area;    // ✅ Notice this is `area`, not `workLocationArea`
    private BorewellType borewellType;
    private MaterialCompanyName companyName;
    private String pipeQuality;
    private Double casingPrice;
    
    private Integer totalDrillingUnit;
    private Double totalDrillingPrice;
    private Integer totalUnitCasing;
    private Double totalCasingPrice;
    private Integer totalUnitMasterCasing;
    private Double masterCasingPricePerUnit;
    private Double totalMasterCasingPrice;
    private Double casingTransporting;
    private Integer totalUnitSloting;
    private Double casingSlotingPerUnit;
    private Double totalSlotingPrice;
    private Integer totalUnitModPowder;
    private Double modPowderPerUnit;
    private Double totalModPowderPrice;
    private Integer totalUnitGravel;
    private Double totalGravelPrice;
    private Integer totalWashingUnit;
    private Double washingPricePerUnit;
    private Double totalWashingPrice;
    private String otherItemDetails;
    private Double otherItemPrice;
    private String gravelPrice;
    private Double totalUnitGravelPrice;
    
    private Integer totalUnitMasterCasing10;
    private Double masterCasing10PricePerUnit;
    private Double totalMaster10CasingPrice;
    private Integer totalUnitMasterCasing12;
    private Double masterCasing12PricePerUnit;
    private Double totalMaster12CasingPrice;
    private Integer totalUnitMasterCasing14;
    private Double masterCasing14PricePerUnit;
    private Double totalMasterCasing14Price;
//    private String msPipeQuality;
//    private Double msPipePrice;
    
    private Double totAmtBeforeDiscount;
    private Double totDiscountAmt;
    private Double totalAmtAfterDiscount;
    private Double cgst;
    private Double sgst;
    private Double grandTotal;

}

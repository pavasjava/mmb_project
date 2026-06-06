package mmb.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mmb.model.BorewellType;
import mmb.model.City;
import mmb.model.Discount;
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate bookingDate;
    private String status;
    private String drillingSize;
    private String otherSize;

    private City city;          
    private WorkLocationArea area;   
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
    private Double gravelPricePerUnit;
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
    
    private Integer totalUnitMC10;
    private Double mc10PricePerUnit;
    private Integer totalUnitMC12;
    private Double mc12PricePerUnit;
    private Integer totalUnitMC14;
    private Double mc14PricePerUnit;
//    private String msPipeQuality;
//    private Double msPipePrice;
    
    private Double totAmtBeforeDiscount;
    private Double totDiscountAmt;
    private Double totalAmtAfterDiscount;
    private Discount discount;
    private Integer cgstPercent;
    private Integer sgstPercent;
    private Double cgst;
    private Double sgst;
    private Double grandTotal;
    private Integer discountPer;
    
    private Integer totalUnit2_5kg;      
    private Double pricePerUnit2_5kg;   
    private Double total2_5kgPrice;     

    private Integer totalUnit6kg;        
    private Double pricePerUnit6kg;    
    private Double total6kgPrice;

}

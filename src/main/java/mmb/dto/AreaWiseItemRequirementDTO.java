package mmb.dto;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class AreaWiseItemRequirementDTO {
	
	private Long requirementId;
    private Integer reqCasingPipe;
    private Integer reqMasterCasingPipe;
    private Integer req10MasterCasingPipe;
    private Integer req12MasterCasingPipe;
    private Integer req14MasterCasingPipe;
    private Integer noOfSloting;
    private Integer washingHours;
    private Integer reqModPowder;
    private Integer reqGravel;
    private Integer reqCasingCone;
    private Integer reqDrillingDepth;
    private String reqDrillingDia;

    private WorkLocationArea area;
    private City city;
    private BorewellType borewellType;

}

package mmb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mmb.model.City;
import mmb.model.WorkLocationArea;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrillingPriceChartDTO {
	
	private Long priceId;
	private City city;
    private WorkLocationArea area;
	private String drillingType;
    private String drillingSize;
    private String otherSize;
    private Double price;
    private String status;

}

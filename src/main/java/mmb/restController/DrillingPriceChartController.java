package mmb.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mmb.repository.DrillingPriceChartRepository;

@RestController
@RequestMapping("/api/prices")
public class DrillingPriceChartController {
	
	@Autowired
    private DrillingPriceChartRepository drillingPriceChartRepository;

	@GetMapping("/findPrice")
	public Double findPrice(
	        @RequestParam Long borewellTypeId,
	        @RequestParam Long cityId,
	        @RequestParam Long locationAreaId,
	        @RequestParam String drillingSize) {

	    System.out.println("borewellTypeId = " + borewellTypeId);
	    System.out.println("cityId = " + cityId);
	    System.out.println("locationAreaId = " + locationAreaId);
	    System.out.println("drillingSize = " + drillingSize);

	    System.out.println("==== Debug Values ====");
	    System.out.printf("borewellTypeId=%d, cityId=%d, locationAreaId=%d, drillingSize='%s'%n",
	                      borewellTypeId, cityId, locationAreaId, drillingSize);
	    Double price = drillingPriceChartRepository.findPrice(
	            borewellTypeId, cityId, locationAreaId, drillingSize.trim()
	    );

	    System.out.println("price -> " + price);

	    return price != null ? price : 0.0;
	}

}

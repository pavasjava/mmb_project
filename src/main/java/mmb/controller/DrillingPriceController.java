package mmb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import mmb.model.BorewellType;
import mmb.model.City;
import mmb.model.DrillingPriceChart;
import mmb.model.WorkLocationArea;
import mmb.repository.BorewellTypeRepository;
import mmb.repository.CityRepository;
import mmb.repository.DrillingPriceChartRepository;
import mmb.repository.WorkLocationAreaRepository;

@Controller
@RequestMapping("/drillingPrice")
public class DrillingPriceController {
	
	@Autowired
	private DrillingPriceChartRepository drillingPriceChartRepository;
	
	@Autowired
    private CityRepository cityRepository;

    @Autowired
    private WorkLocationAreaRepository workLocationAreaRepository;
    
    @Autowired
    private BorewellTypeRepository borewellTypeRepository;
    
	@GetMapping("/getAllPrices")
    public String getAllPriceDetails(Model model) {
        List<DrillingPriceChart> drillingPrices = (List<DrillingPriceChart>) drillingPriceChartRepository.findAll();
        model.addAttribute("drillingPrices", drillingPrices);
        return "drillingPrice/drillingPriceDetails";
    }
	
	@GetMapping("/addNewDrillingPriceDtls")
    public String addNewRawMaterial(Model model) {
		model.addAttribute("drillingPriceChart", new DrillingPriceChart());

        // Load all cities only
        List<City> cities = cityRepository.findAll();
        model.addAttribute("cities", cities);
        
        // Load all borewellTypes only
        List<BorewellType> borewellTypes = borewellTypeRepository.findAll();
        model.addAttribute("borewellTypes", borewellTypes);

        return "drillingPrice/addNewPriceDtls";
    }
	
	@PostMapping("/savePriceDtls")
    public String saveDrillingPriceChart(@ModelAttribute DrillingPriceChart drillingPriceChart) {
		drillingPriceChart.setStatus("1");
		drillingPriceChartRepository.save(drillingPriceChart);
		System.out.println("City -> "+drillingPriceChart.getCity());
		System.out.println("Area -> "+drillingPriceChart.getWorkLocationArea());
		System.out.println("Size -> "+drillingPriceChart.getDrillingSize());
		System.out.println("Other Size -> "+drillingPriceChart.getOtherSize());
		System.out.println("Price -> "+drillingPriceChart.getPrice());
		System.out.println("Status -> "+drillingPriceChart.getStatus());
        return "redirect:/drillingPrice/getAllPrices";
    }
	
	@GetMapping("/editPriceDtls/{id}")
    public String editPriceDtls(@PathVariable("id") Long id, Model model) {
		DrillingPriceChart drillingPriceChart = drillingPriceChartRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Price Id:" + id));
        model.addAttribute("drillingPriceChart", drillingPriceChart);
        // Load all cities for the dropdown
        List<City> cities = cityRepository.findAll();
        model.addAttribute("cities", cities);

        // Load areas for the selected city so Thymeleaf can preselect
        Long cityId = drillingPriceChart.getCity().getCityId();
        List<WorkLocationArea> areas = workLocationAreaRepository.findByCity_CityId(cityId);
        model.addAttribute("areas", areas);
        
        List<BorewellType> borewellTypes = borewellTypeRepository.findAll();
        model.addAttribute("borewellTypes", borewellTypes);
        
        return "drillingPrice/addNewPriceDtls";
    }

    @GetMapping("/deletePriceDtls/{id}")
    public String deletePriceDtls(@PathVariable("id") Long id) {
    	drillingPriceChartRepository.deleteById(id);
        return "redirect:/drillingPrice/getAllPrices";
    }

}

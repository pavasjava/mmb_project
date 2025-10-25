package mmb.service;

import java.util.List;

import mmb.dto.BorewellTypeDTO;
import mmb.dto.DrillingPriceChartDTO;

public interface DrillingPriceService {
	
	public DrillingPriceChartDTO savePriceChart(DrillingPriceChartDTO dto);
	public List<DrillingPriceChartDTO> getAllPriceChart();
	DrillingPriceChartDTO getById(Long id);
    void deleteById(Long id);

}

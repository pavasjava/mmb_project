package mmb.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mmb.dto.DrillingPriceChartDTO;
import mmb.model.DrillingPriceChart;
import mmb.repository.DrillingPriceChartRepository;
import mmb.service.DrillingPriceService;

@Service
public class DrillingPriceServiceImpl implements DrillingPriceService {
	
	private final DrillingPriceChartRepository drillingPriceChartRepo;

    public DrillingPriceServiceImpl(DrillingPriceChartRepository drillingPriceChartRepo) {
        this.drillingPriceChartRepo = drillingPriceChartRepo;
    }
    
    @Autowired
	private ModelMapper modelMapper;

	@Override
	public DrillingPriceChartDTO savePriceChart(DrillingPriceChartDTO dto) {
		DrillingPriceChart drillingPriceChart = mapToEntity(dto);
		DrillingPriceChart saved = drillingPriceChartRepo.save(drillingPriceChart);
        return mapToDTO(saved);
	}

	private DrillingPriceChart mapToEntity(DrillingPriceChartDTO dto) {
	    DrillingPriceChart entity = new DrillingPriceChart();
	    entity.setPriceId(dto.getPriceId());
	    entity.setCity(dto.getCity());
	    entity.setWorkLocationArea(dto.getArea());
	    entity.setPrice(dto.getPrice());
	    entity.setDrillingType(dto.getDrillingType());
	    entity.setDrillingSize(dto.getDrillingSize());
	    entity.setStatus(dto.getStatus());
	    return entity;
	}

	private DrillingPriceChartDTO mapToDTO(DrillingPriceChart entity) {
	    DrillingPriceChartDTO dto = new DrillingPriceChartDTO();
	    entity.setPriceId(dto.getPriceId());
	    entity.setCity(dto.getCity());
	    entity.setWorkLocationArea(dto.getArea());
	    entity.setPrice(dto.getPrice());
	    entity.setDrillingType(dto.getDrillingType());
	    entity.setDrillingSize(dto.getDrillingSize());
	    entity.setStatus(dto.getStatus());
	    return dto;
	}

	@Override
	public List<DrillingPriceChartDTO> getAllPriceChart() {
		return drillingPriceChartRepo.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
	}

	@Override
	public DrillingPriceChartDTO getById(Long id) {
		DrillingPriceChart drillingPriceChart = drillingPriceChartRepo.findById(id).orElse(null);
		return drillingPriceChart != null ? modelMapper.map(drillingPriceChart, DrillingPriceChartDTO.class) : null;
	}

	@Override
	public void deleteById(Long id) {
		drillingPriceChartRepo.deleteById(id);
	}

}

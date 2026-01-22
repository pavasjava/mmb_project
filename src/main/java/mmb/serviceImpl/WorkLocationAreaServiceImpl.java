package mmb.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mmb.model.WorkLocationArea;
import mmb.repository.WorkLocationAreaRepository;
import mmb.service.WorkLocationAreaService;

@Service
public class WorkLocationAreaServiceImpl implements WorkLocationAreaService{
	
	@Autowired
    private WorkLocationAreaRepository workLocationAreaRepository;

	@Override
	public List<WorkLocationArea> getAreasByCityId(Long cityId) {
		return workLocationAreaRepository.findByCity_CityId(cityId);
	}

}

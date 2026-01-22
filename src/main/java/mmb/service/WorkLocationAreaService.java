package mmb.service;

import java.util.List;

import mmb.model.WorkLocationArea;

public interface WorkLocationAreaService {
	
	public List<WorkLocationArea> getAreasByCityId(Long cityId);

}

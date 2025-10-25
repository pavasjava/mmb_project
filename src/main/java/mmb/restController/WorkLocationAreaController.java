package mmb.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mmb.model.WorkLocationArea;
import mmb.repository.WorkLocationAreaRepository;

@RestController
@RequestMapping("/api/areas")
public class WorkLocationAreaController {
	
	@Autowired
    private WorkLocationAreaRepository workLocationAreaRepository;

    @GetMapping("/byCity/{cityId}")
    public List<WorkLocationArea> getAreasByCity(@PathVariable Long cityId) {
        return workLocationAreaRepository.findByCity_CityId(cityId);
    }

}

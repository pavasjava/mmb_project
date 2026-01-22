package mmb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import mmb.dto.AreaWiseItemRequirementDTO;
import mmb.dto.BorewellTypeDTO;
import mmb.model.BorewellType;
import mmb.model.City;
import mmb.model.WorkLocationArea;
import mmb.repository.CityRepository;
import mmb.repository.WorkLocationAreaRepository;
import mmb.service.AreaWiseItemRequirementService;
import mmb.service.BorewellTypeService;
import mmb.service.WorkLocationAreaService;

@Controller
@RequestMapping("/requirements")
public class AreaWiseItemRequirementController {

	private final UserDetailsService userDetails;

	@Autowired
	private AreaWiseItemRequirementService requirementService;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private WorkLocationAreaService workLocationAreaService;

	@Autowired
	private BorewellTypeService borewellTypeService;

	AreaWiseItemRequirementController(UserDetailsService userDetails) {
		this.userDetails = userDetails;
	}

	@GetMapping
	public String listRequirements(Model model) {
		// Load all requirements for the list
		List<AreaWiseItemRequirementDTO> requirements = requirementService.getAllRequirements();
		model.addAttribute("requirements", requirements);
		return "requirement/requirements";
	}

	@GetMapping("/addNewAreaWiseReq")
	public String viewAllRequirements(Model model) {
		List<AreaWiseItemRequirementDTO> requirements = requirementService.getAllRequirements();

		List<City> cities = cityRepository.findAll();
		model.addAttribute("cities", cities);

		List<BorewellTypeDTO> borewellType = borewellTypeService.getAllTypes();
		model.addAttribute("borewellTypes", borewellType);

		model.addAttribute("requirements", requirements);
		model.addAttribute("requirement", new AreaWiseItemRequirementDTO());
		return "requirement/addNewRequirements";
	}

	@PostMapping("/save")
	public String saveRequirement(@ModelAttribute("requirement") AreaWiseItemRequirementDTO dto) {
		requirementService.saveRequirement(dto);
		return "redirect:/requirements";
	}

	@GetMapping("/edit/{id}")
	public String editRequirement(@PathVariable Long id, Model model) {
		AreaWiseItemRequirementDTO requirement = requirementService.getRequirementById(id);
		City city = requirement.getCity();
		WorkLocationArea area = requirement.getArea();
		BorewellType borewellType = requirement.getBorewellType();
		List<BorewellTypeDTO> borewellTypes = borewellTypeService.getAllTypes();
		model.addAttribute("city", city);
		model.addAttribute("area", area);
		model.addAttribute("borewellType", borewellType);
		model.addAttribute("borewellTypes", borewellTypes);
		model.addAttribute("requirement", requirement);
		model.addAttribute("requirements", requirementService.getAllRequirements());
		return "requirement/addNewRequirements";
	}

	@PostMapping("/update/{id}")
	public String updateRequirement(@PathVariable Long id,
			@ModelAttribute("requirement") AreaWiseItemRequirementDTO dto) {
		requirementService.updateRequirement(id, dto);
		return "redirect:/requirements";
	}

	@GetMapping("/delete/{id}")
	public String deleteRequirement(@PathVariable Long id) {
		requirementService.deleteRequirement(id);
		return "redirect:/requirements";
	}
}

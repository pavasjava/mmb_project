package mmb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import mmb.model.RawMaterial;
import mmb.repository.MaterialCompanyNameRepo;
import mmb.repository.MaterialTypeRepo;
import mmb.repository.RawMaterialRepo;

@Controller
@SessionAttributes("token")
public class RawMaterialController {
	
	@Autowired
    private RawMaterialRepo rawMaterialRepository;
	
	@Autowired
	private MaterialTypeRepo materialTypeRepository;
	@Autowired
    private MaterialCompanyNameRepo materialCompanyNameRepository;

    @GetMapping("/getAllRawMaterials")
    public String getAllRawMaterials(Model model) {
        List<RawMaterial> materials = (List<RawMaterial>) rawMaterialRepository.findAll();
        model.addAttribute("items", materials);
        return "rawmaterial/rawMaterialsDetails";
    }

    @GetMapping("/addNewRawMaterial")
    public String addNewRawMaterial(Model model) {
        model.addAttribute("rawMaterial", new RawMaterial());
        model.addAttribute("materialTypes", materialTypeRepository.findAll());
        model.addAttribute("companies", materialCompanyNameRepository.findAll());
        return "rawmaterial/addNewRawMaterial";
    }

    @PostMapping("/saveRawMaterial")
    public String saveRawMaterial(@ModelAttribute RawMaterial rawMaterial) {
        rawMaterialRepository.save(rawMaterial);
        return "redirect:/getAllRawMaterials";
    }

    @GetMapping("/editRawMaterial/{id}")
    public String editRawMaterial(@PathVariable("id") Integer id, Model model) {
        RawMaterial material = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid RawMaterial Id:" + id));
        model.addAttribute("rawMaterial", material);
        model.addAttribute("materialTypes", materialTypeRepository.findAll());
        model.addAttribute("companies", materialCompanyNameRepository.findAll());
        return "rawmaterial/addNewRawMaterial";
    }

    @GetMapping("/deleteRawMaterial/{id}")
    public String deleteRawMaterial(@PathVariable("id") Integer id) {
        rawMaterialRepository.deleteById(id);
        return "redirect:/getAllRawMaterials";
    }
	

}

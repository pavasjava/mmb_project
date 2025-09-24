package mmb.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import mmb.dto.BorewellTypeDTO;
import mmb.service.BorewellTypeService;

@Controller
@RequestMapping("/borewellTypes")
public class BorewellTypeController {

    private final BorewellTypeService borewellTypeService;

    public BorewellTypeController(BorewellTypeService borewellTypeService) {
        this.borewellTypeService = borewellTypeService;
    }

    @GetMapping("/showTypeAddForm")
    public String showForm(Model model) {
        model.addAttribute("borewellType", new BorewellTypeDTO());
        return "booking/borewell-type-form";
    }

    @PostMapping("/saveBorewellType")
    public String saveBorewellType(@ModelAttribute BorewellTypeDTO dto) throws IOException {
        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            String fileName = StringUtils.cleanPath(dto.getImageFile().getOriginalFilename());
            String uploadDir = "uploads/borewell-images/";

            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) uploadPath.mkdirs();

            Path filePath = Paths.get(uploadDir, fileName);
            try (InputStream inputStream = dto.getImageFile().getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            dto.setImageUrl("/" + uploadDir + fileName);
        }

        borewellTypeService.saveBorewellType(dto);
        return "redirect:/borewellTypes/borewellTypeList";
    }


    @GetMapping("/borewellTypeList")
    public String listTypes(Model model) {
        model.addAttribute("types", borewellTypeService.getAllTypes());
        return "booking/borewell-type-list";
    }
    
    @GetMapping("/editBorewellType/{id}")
    public String editBorewellType(@PathVariable Long id, Model model) {
        BorewellTypeDTO dto = borewellTypeService.getById(id);
        model.addAttribute("borewellType", dto);
        return "editBorewellType";
    }

    @PostMapping("/update")
    public String updateBorewellType(@ModelAttribute BorewellTypeDTO dto) throws IOException {
        borewellTypeService.saveBorewellType(dto);
        return "redirect:/borewellTypes/BorewellTypeList";
    }

    @GetMapping("/deleteBorewellType/{id}")
    public String deleteBorewellType(@PathVariable Long id) {
        borewellTypeService.deleteById(id);
        return "redirect:/borewellTypes/borewellTypeList";
    }
}

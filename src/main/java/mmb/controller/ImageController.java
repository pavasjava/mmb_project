package mmb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("token")
public class ImageController {

	@GetMapping("/showImages")
    public String showImagesPage() {
        return "show-image";
    }
	@GetMapping("/articles")
    public String showArticles() {
        return "article/articles";
    }
}

package mmb.controller;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import mmb.dto.LoginRequest;
import mmb.exception.UserNotFoundException;
import mmb.jwt.JwtService;
import mmb.model.UserInfo;
import mmb.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("token")
public class UserController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserService userService;

	@GetMapping("/register")
	public String showRegistrationForm() {
		return "register";
	}

	@PostMapping("/registerreq")
	public String registerUser(@ModelAttribute("user") UserInfo userInfo, Model model) {
		String result = userService.register(userInfo);
		model.addAttribute("message", result);
		return "redirect:/login";
	}

	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("user", new UserInfo());
		return "login";
	}

	@PostMapping("/loginreq")
	public String authenticationAndGetToken(@ModelAttribute("user") LoginRequest authRequest, Model model, HttpSession session) {
	    try {
	        Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
	        if (authentication.isAuthenticated()) {
	            List<String> roles = authentication.getAuthorities().stream()
	                    .map(GrantedAuthority::getAuthority)
	                    .collect(Collectors.toList());
	            System.out.println("Authenticated Roles: " + roles);
	            String token = jwtService.generateToken(authRequest.getUsername(), roles);
	            model.addAttribute("token", token);
	            session.setAttribute("token", token);
	            System.out.println("ROLES"+roles);

	            if (roles.contains("ROLE_USER")) {
	                return "redirect:/permission";
	            } else if (roles.contains("ROLE_ADMIN")) {
//	                return "redirect:/allUser";
	            	return "redirect:/home";
	            } else if (roles.contains("ROLE_NURSE")) {
	                return "redirect:/dashboard";
	            } else if (roles.contains("ROLE_DOCTOR")) {
	                return "redirect:/dashboard";
	            } else if (roles.contains("ROLE_PHARMASSIST")) {
	                return "redirect:/dashboard";
	            } else {
	                return "redirect:/login";
	            }
	        } else {
	            model.addAttribute("error", "Invalid credentials");
	            return "login";
	        }
	    } catch (Exception e) {
	        model.addAttribute("error", "Authentication failed: " + e.getMessage());
	        return "login";
	    }
	}
	
	@GetMapping("/dashboard")
	public String showDash(Model model) {
		return "home";
	}
	
	@GetMapping("/home")
	public String showHomePage(@ModelAttribute("token") String token, Model model) {
//		System.out.println("Token: ");
	    return "home";
	}

	@GetMapping("/allUser")
	public String showAllUsers(@ModelAttribute("token") String token, Model model) {
		System.out.println("Token: ");
		List<UserInfo> users = userService.allUser();
	    model.addAttribute("users", users);
	    return "alluser";
	}
	
	@PutMapping("/updateRole")
	public String updateType(@RequestParam String roles, @RequestParam int id, Model model) {
		System.out.println("Received id: " + id + ", roles: " + roles);
	    userService.updateRole(id, roles);
	    return "redirect:/allUser";
	}
	
	@PutMapping("/updateStatus/{id}/{status}")
    public String updateLoginStatus(@PathVariable int id, @PathVariable int status, Model model) {
       userService.updateLoginStatus(id, status);
        return "redirect:/alluser";
    }
	
	@GetMapping("/permission")
	public String permission(Model model) {
		model.addAttribute("user", new UserInfo());
		return "permission";
	}
//	@GetMapping("/showImages")
//    public String showImagesPage() {
//        return "show-image";
//    }
//	@GetMapping("/articles")
//    public String showArticles() {
//        return "article/articles";
//    }
}

package mmb.controller;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import mmb.dto.LoginRequest;
import mmb.jwt.JwtService;
import mmb.model.UserInfo;
import mmb.repository.UserRepository;
import mmb.service.UserService;

@Controller
public class LoginController {

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	JwtService jwtService;

	@Autowired
	AuthenticationManager authenticationManager;

//	@GetMapping("/index")
//	public String index(Model model) {
//		return "menu/index";
//	}

	@GetMapping("/index")
	public String showRegistrationForm(Model model) {
		return "login/register";
	}
	
	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("user") UserInfo userInfo, BindingResult bindingResult,
	        RedirectAttributes redirectAttributes, Model model) {

	    if (bindingResult.hasErrors()) {
	        return "register";
	    }

	    try {
	        String result = userService.register(userInfo);
	        model.addAttribute("message", result);
	        return "redirect:/login";
	    } catch (RuntimeException e) {
	        if (e.getMessage().equals("Email is already taken!")) {
	            return "redirect:/index?error=email";
	        }
	        return "register";
	    }
	}
//	@GetMapping("/register")
//	public String showRegistrationForm() {
//		return "register";
//	}	


	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("user", new UserInfo());
		return "login/login";
	}



	@PostMapping("/loginreq")
	public String authenticationAndGetToken(@ModelAttribute("user") LoginRequest authRequest, Model model,
			HttpSession session) {
		System.out.println("Authenticated Roles: ");
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

			if (authentication.isAuthenticated()) {
				List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
						.collect(Collectors.toList());
				System.out.println("Authenticated Roles: " + roles);
				String token = jwtService.generateToken(authRequest.getUsername(), roles);
				model.addAttribute("token", token);
				session.setAttribute("token", token);
				session.setAttribute("username", authRequest.getUsername());
				session.setAttribute("roles", roles);
				System.out.println("ROLES: " + roles);

				Optional<UserInfo> userInfoOpt = userRepository.findByEmail(authRequest.getUsername());
				System.out.println("userInfoOpt -> "+ userInfoOpt.get());
				if (userInfoOpt.isPresent()) {
					UserInfo userInfo = userInfoOpt.get();
					int status = userInfo.getStatus();

					if (roles.contains("ROLE_USER")) {
						if (status == 1) {
							return "redirect:/dashboard";
						} else {
							return "redirect:/permission";
						}
					} else if (roles.contains("ROLE_ADMIN")) {
						return "redirect:/dashboard";
					} else if (roles.contains("ROLE_EMPLOYEE")) {
						return "redirect:/permission";
					} else {
						return "redirect:/login";
					}
				}
			}

			// If authentication fails
			return "redirect:/login?error=Invalid%20credentials";

		} catch (Exception e) {
			return "redirect:/login?error=Authentication%20failed";
		}
	}

	@GetMapping("/permission")
	public String permission(Model model) {
		model.addAttribute("user", new UserInfo());
		return "login/permission";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login?logout";
	}

	@GetMapping("/dashboard")
	public String showDash(Model model) {
		return "home";
	}

	@GetMapping("/allUser")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String showAllUsers(@ModelAttribute("token") String token, Model model) {
		List<UserInfo> users = userService.allUser();
		model.addAttribute("users", users);
		return "login/alluser";
	}

	@PostMapping("/updateRole")
	public String updateType(@RequestParam("roles") String roles, @RequestParam("id") int id, Model model) {
		System.out.println("Received id: " + id + ", roles: " + roles);
		userService.updateRole(id, roles);
		return "redirect:/allUser";
	}

	@PostMapping("/updateStatus/{id}/{status}")
	public String updateLoginStatus(@PathVariable("id") int id, @PathVariable("status") int status, Model model) {
		System.out.println("hello");
		userService.updateLoginStatus(id, status);
		return "redirect:/allUser";
	}

	@GetMapping("/updateCredentials")
	public String showUpdateCredentialsForm(Model model) {
		return "login/update-credentials";
	}

	@PostMapping("/updateUserNameAndPwd")
	public String updatePassword(@ModelAttribute("user") UserInfo userInfo, Model model) {
		String result = userService.updateUserPassword(userInfo);
		model.addAttribute("message", result);
		return "redirect:/login";
	}
}

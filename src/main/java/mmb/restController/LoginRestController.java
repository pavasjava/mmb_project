package mmb.restController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import mmb.dto.LoginRequest;
import mmb.jwt.JwtService;
import mmb.model.UserInfo;
import mmb.service.UserService;

@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://localhost:5000")
public class LoginRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

//    @PostMapping("/register")
//    public ResponseEntity<Map<String, String>> registerUser(@RequestBody UserInfo userInfo) {
//        String result = userService.register(userInfo);
//        Map<String, String> response = new HashMap<>();
//        response.put("message", result);
//        return ResponseEntity.ok(response);
//    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserInfo userInfo) {
        try {
            String result = userService.register(userInfo);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (RuntimeException e) {
            if ("Email is already taken!".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                     .body(Map.of("error", "Email is already taken!"));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "Something went wrong!"));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest authRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(), 
                            authRequest.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                List<String> roles = authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());

                String token = jwtService.generateToken(authRequest.getUsername(), roles);

                response.put("token", token);
                response.put("roles", roles);

                // ✅ Decide redirect path based on role
                String redirect;
                if (roles.contains("ROLE_USER")) {
                    redirect = "/permission";
                } else if (roles.contains("ROLE_ADMIN")) {
                    redirect = "/home";
                } else if (roles.contains("ROLE_NURSE") 
                        || roles.contains("ROLE_DOCTOR") 
                        || roles.contains("ROLE_PHARMASSIST")) {
                    redirect = "/dashboard";
                } else {
                    redirect = "/login";
                }
                response.put("redirect", redirect);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid credentials"));
            }

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication failed: " + e.getMessage()));
        }
    }


//    @PostMapping("/login")
//    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest authRequest) {
//        Map<String, Object> response = new HashMap<>();
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
//
//            if (authentication.isAuthenticated()) {
//                List<String> roles = authentication.getAuthorities().stream()
//                        .map(GrantedAuthority::getAuthority)
//                        .collect(Collectors.toList());
//
//                String token = jwtService.generateToken(authRequest.getUsername(), roles);
//                response.put("token", token);
//                response.put("roles", roles);
//
//                // Optional: redirect info for mobile app navigation
//                if (roles.contains("ROLE_USER")) {
//                    response.put("redirect", "/permission");
//                } else if (roles.contains("ROLE_ADMIN")) {
//                    response.put("redirect", "/home");
//                } else if (roles.contains("ROLE_NURSE") || roles.contains("ROLE_DOCTOR") || roles.contains("ROLE_PHARMASSIST")) {
//                    response.put("redirect", "/dashboard");
//                } else {
//                    response.put("redirect", "/login");
//                }
//
//                return ResponseEntity.ok(response);
//            } else {
//                response.put("error", "Invalid credentials");
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//            }
//
//        } catch (Exception e) {
//            response.put("error", "Authentication failed: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//        }
//    }

    // ✅ Get all users
    @GetMapping
    public ResponseEntity<List<UserInfo>> getAllUsers() {
        List<UserInfo> users = userService.allUser();
        return ResponseEntity.ok(users);
    }

    // ✅ Update role
    @PutMapping("/updateRole")
    public ResponseEntity<Map<String, String>> updateRole(@RequestParam String roles, @RequestParam int id) {
        userService.updateRole(id, roles);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Role updated successfully");
        return ResponseEntity.ok(response);
    }

    // ✅ Update login status
    @PutMapping("/updateStatus/{id}/{status}")
    public ResponseEntity<Map<String, String>> updateLoginStatus(@PathVariable int id, @PathVariable int status) {
        userService.updateLoginStatus(id, status);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Status updated successfully");
        return ResponseEntity.ok(response);
    }

    // ✅ Permission endpoint (for mobile, just return user info if needed)
    @GetMapping("/permission")
    public ResponseEntity<UserInfo> getPermission() {
        return ResponseEntity.ok(new UserInfo());
    }
    
    @GetMapping("/updateCredentials")
    public ResponseEntity<Map<String, String>> showUpdateCredentials() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is the update credentials page. Implement your forgot password logic here.");
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/updatePassword")
//    public ResponseEntity<Map<String, String>> updatePassword(@RequestBody UserInfo userInfo) {
//        String result = userService.updateUserPassword(userInfo);
//        Map<String, String> response = new HashMap<>();
//        response.put("message", result);
//        return ResponseEntity.ok(response);
//    }
    
    @PostMapping("/updatePassword")
    public ResponseEntity<Map<String, String>> updatePassword(@RequestBody UserInfo userInfo) {
        String result = userService.updateUserPassword(userInfo);
        Map<String, String> response = new HashMap<>();
        response.put("message", result);

        if ("Email not available".equals(result)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response); // 404
        }

        return ResponseEntity.ok(response); // 200 if success
    }
}


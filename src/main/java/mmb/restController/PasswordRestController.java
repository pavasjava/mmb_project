package mmb.restController;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.internet.MimeMessage;
import mmb.model.UserInfo;
import mmb.service.UserService;

@RestController
@RequestMapping("/api/pwdValidation")
public class PasswordRestController {
	
	private final UserService userService;
    private final JavaMailSender mailSender;

    public PasswordRestController(UserService userService, JavaMailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
    }

    private Map<String, String> otpStore = new HashMap<>(); // email -> OTP

    @PostMapping("/requestOtp")
    public ResponseEntity<Map<String, String>> requestOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, String> response = new HashMap<>();

        // Validate email
        if (email == null || email.isBlank()) {
            response.put("message", "Email is required");
            return ResponseEntity.badRequest().body(response);
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            response.put("message", "Invalid email format");
            return ResponseEntity.badRequest().body(response);
        }

        // Check if user exists
        Optional<UserInfo> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            response.put("message", "Email not available");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Generate OTP
        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
        otpStore.put(email, otp);

        try {
            // Prepare HTML email with logo
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(email);
            helper.setSubject("OTP for Password Update - Maa Mangala Borwell");

            String htmlMsg = "<html>" +
                    "<body>" +
                    "<div style='font-family: Arial, sans-serif; line-height: 1.6;'>" +
                    "<p>Dear User,</p>" +
                    "<p>You have requested to update your password for your account associated with this email address.</p>" +
                    "<p><strong>OTP: " + otp + "</strong></p>" +
                    "<p>This OTP is valid for the next 10 minutes. Do not share it with anyone.</p>" +
                    "<p>If you did not request this, please ignore this email or contact our support team.</p>" +
                    "<br/>" +
                    "<p>Thank you,<br/>Maa Mangala Borwell</p>" +
                    "<img src='cid:logoImage' alt='Maa Mangala Borwell' style='width:150px;'/><br/><br/>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlMsg, true);

            // Add logo as inline image
            FileSystemResource logo = new FileSystemResource(new File("src/main/resources/static/images/MMBLogo1.jpg")); // Adjust path
            helper.addInline("logoImage", logo);

            mailSender.send(mimeMessage);

            response.put("message", "OTP sent to your email");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Failed to send OTP. Please try again.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    @PostMapping("/updatePasswordWithOtp")
    public ResponseEntity<Map<String, String>> updatePasswordWithOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        String password = request.get("password");

        Map<String, String> response = new HashMap<>();

        if (email == null || email.isBlank() || otp == null || otp.isBlank() || password == null || password.isBlank()) {
            response.put("message", "Email, OTP, and Password are required");
            return ResponseEntity.badRequest().body(response);
        }

        // Check OTP
        if (!otp.equals(otpStore.get(email))) {
            response.put("message", "Invalid OTP");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Create UserInfo object using setters
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);
        userInfo.setPassword(password);

        // Update password
        String result = userService.updateUserPassword(userInfo);
        otpStore.remove(email);

        response.put("message", result);
        return ResponseEntity.ok(response);
    }
}

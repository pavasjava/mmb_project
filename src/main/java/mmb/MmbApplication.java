package mmb;

import java.util.Properties;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;

@SpringBootApplication
public class MmbApplication {
	
//	@Autowired
//    private JwtService jwtService;

	public static void main(String[] args) {
		SpringApplication.run(MmbApplication.class, args);
	}
	
//	@Bean
//    public CommandLineRunner testSmtpConnection() {
//        return args -> {
//            final String username = "maamangalaborewell@gmail.com";
//            final String password = "cfliczcwmjizbowt"; // 16-char App Password
//
//            Properties props = new Properties();
//            props.put("mail.smtp.auth", "true");
//            props.put("mail.smtp.starttls.enable", "true");
//            props.put("mail.smtp.host", "smtp.gmail.com");
//            props.put("mail.smtp.port", "587");
//
//            Session session = Session.getInstance(props,
//                new jakarta.mail.Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(username, password);
//                    }
//                });
//
//            try {
//                Transport transport = session.getTransport("smtp");
//                transport.connect();
//                System.out.println("✅ Connected to Gmail SMTP successfully!");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        };
//    }
	
	// Runs after Spring context is initialized
//    @Bean
//    public CommandLineRunner printTokenOnStartup() {
//        return args -> {
//            String username = "admin";
//            List<String> roles = List.of("ROLE_ADMIN");
//
//            // Generate token (this will call your generateToken() method)
//            String token = jwtService.generateToken(username, roles);
//
//            // Token is already printed inside generateToken(), or you can print again
//            System.out.println("Token at startup: " + token);
//        };
//    }

}

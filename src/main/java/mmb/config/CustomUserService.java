package mmb.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import mmb.model.UserInfo;
import mmb.repository.UserRepository;

@Component
public class CustomUserService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<UserInfo> userName = userRepo.findByEmail(email);
		return userName.map(CustomUser::new).orElseThrow(()->new UsernameNotFoundException("User Not Found"+email));
	}

}
package mmb.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import mmb.exception.UserNotFoundException;
import mmb.model.UserInfo;
import mmb.repository.UserRepository;
import mmb.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public String register(UserInfo userInfo) {
		userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
		userInfo.setRoles("ROLE_USER");
		int checkEmail = userRepository.countByEmail(userInfo.getEmail());
		if ( checkEmail > 0) {
            throw new RuntimeException ("Email is already taken!");
        }
		userRepository.save(userInfo);
		return "Register SuccesFully";
	}
	
	@Override
	public String updateRole(int id, String roles) {
		
		UserInfo user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User with ID \" + id + \" not found."));
		user.setRoles(roles);
		userRepository.save(user);
		return "Update Successfully";
	}

	@Override
	public List<UserInfo> allUser() {
		List<UserInfo> alldata = userRepository.findAll();
		return alldata.stream().filter(user -> {
			return !"ROLE_ADMIN".equals(user.getRoles());
		}).collect(Collectors.toList());
	}

	@Override
	public String updateLoginStatus(int id, int status) {
		UserInfo user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User with ID \" + id + \" not found."));
		user.setStatus(status);
		userRepository.save(user);
		System.out.println("hello1");
		return "Status updated";
	}
	
//	  @Override
//	  public String updateUserPassword(UserInfo userInfo) {
//	        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
//	        userRepository.updatePasswordByEmail(userInfo.getPassword(), userInfo.getEmail());
//	        return "updated userName and Password SuccesFully";
//	    }
	  
//	  @Override
//	  public String updateUserPassword(UserInfo userInfo) {
//	        Optional<UserInfo> userOpt = userRepository.findByEmail(userInfo.getEmail());
//
//	        if (userOpt.isPresent()) {
//	        	userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
//	        	userRepository.updatePasswordByEmail(userInfo.getPassword(), userInfo.getEmail());
//	            return "Credentials updated successfully!";
//	        } else {
//	            return "Email not available";
//	        }
//	    }
	
	@Override
    public Optional<UserInfo> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public String updateUserPassword(UserInfo userInfo) {
        Optional<UserInfo> userOpt = userRepository.findByEmail(userInfo.getEmail());

        if (userOpt.isPresent()) {
            String encodedPassword = passwordEncoder.encode(userInfo.getPassword());
            userRepository.updatePasswordByEmail(encodedPassword, userInfo.getEmail());
            return "Credentials updated successfully!";
        } else {
            return "Email not available";
        }
    }
}

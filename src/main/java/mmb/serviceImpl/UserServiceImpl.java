package mmb.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import mmb.exception.UserNotFoundException;
import mmb.model.UserInfo;
import mmb.repository.UserInfoRepo;
import mmb.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserInfoRepo userRepo;

	@Override
	public String register(UserInfo userInfo) {
		userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
		userInfo.setRoles("ROLE_USER");
		userRepo.save(userInfo);
		return "Register SuccesFully";
	}

	@Override
	public String updateRole(int id, String roles ) {
		UserInfo user = userRepo.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User with ID \" + id + \" not found."));
		user.setRoles(roles);
		userRepo.save(user);
		return "Update Successfully";
	}

	@Override
	public List<UserInfo> allUser() {
		List<UserInfo> alldata = userRepo.findAll();
		return alldata;
	}

	@Override
	public String updateLoginStatus(int id,int status) {
		UserInfo user = userRepo.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User with ID \" + id + \" not found."));
		user.setStatus(status);
		userRepo.save(user);
		return "Status updated";
	}
}

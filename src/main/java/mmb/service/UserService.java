package mmb.service;

import java.util.List;
import java.util.Optional;

import mmb.model.UserInfo;

public interface UserService {

	public String register(UserInfo userInfo);

	public String updateRole(int id, String roles);

	public List<UserInfo> allUser();

	public String updateLoginStatus(int id, int status);
	
	public String updateUserPassword(UserInfo userInfo);
	
	Optional<UserInfo> findByEmail(String email);

}

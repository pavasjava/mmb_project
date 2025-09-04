package mmb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mmb.model.UserInfo;

@Repository
public interface UserInfoRepo extends JpaRepository<UserInfo, Integer>{
	
	Optional<UserInfo>findByEmail(String email);

}

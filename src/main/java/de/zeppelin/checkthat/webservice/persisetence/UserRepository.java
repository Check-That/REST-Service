package de.zeppelin.checkthat.webservice.persisetence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.zeppelin.checkthat.webservice.models.user.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	
	public User findOneByAuthId(Long authId);
	public int countByauthId(Long authId);

}

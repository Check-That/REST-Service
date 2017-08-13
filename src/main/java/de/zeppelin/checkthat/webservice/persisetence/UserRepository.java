package de.zeppelin.checkthat.webservice.persisetence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.zeppelin.checkthat.webservice.models.user.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	public User findOneByAuthId(Long authId);

	public User findOneByUniqueString(String uniqueString);

	public int countByauthId(Long authId);

	public int countByUniqueString(String uniqueString);

	@Query("SELECT case when count(u) > 0 then 'true' else 'false' end from user u where u.uniqueString = :uniqueString")
	public Boolean existsByUniqueString(@Param("uniqueString") String uniqueString);

	@Query("SELECT case when count(u) > 0 then 'true' else 'false' end from user u where u.phone = :phone")
	public Boolean existsByPhoneNumber(@Param("phone") String phone);

	@Query("SELECT case when count(u) > 0 then 'true' else 'false' end from user u where u.email = :email")
	public Boolean existsByEmail(@Param("email") String email);
}
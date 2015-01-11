package de.zeppelin.checkthat.webservice.persicetence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.zeppelin.checkthat.webservice.Models.user.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}

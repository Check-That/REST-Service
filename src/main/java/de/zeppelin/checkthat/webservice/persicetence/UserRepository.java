package de.zeppelin.checkthat.webservice.persicetence;

import org.springframework.data.repository.CrudRepository;

import de.zeppelin.checkthat.webservice.Models.User;

public interface UserRepository extends CrudRepository<User, Long> {

}
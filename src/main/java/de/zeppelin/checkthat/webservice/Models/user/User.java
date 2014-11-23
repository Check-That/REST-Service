package de.zeppelin.checkthat.webservice.Models.user;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity(name="user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	@NotNull
	public String name;
	@Embedded
	public Privacy privacy = new Privacy();
	public String imagePath = "";

	protected User() {
	}

	public User(String name) {
		this.name = name;
	}
}

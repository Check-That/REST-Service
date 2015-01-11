package de.zeppelin.checkthat.webservice.Models.user;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;

import de.zeppelin.checkthat.webservice.persicetence.UserRepository;

@Entity(name = "user")
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	@NotNull
	public String name;
	@Embedded
	public Privacy privacy = new Privacy();
	@Column(name = "image_path")
	public String imagePath = "";

	@Transient
	@Autowired
	private UserRepository userRep;

	protected User() {
	}

	public User(String name) {
		this.name = name;
	}
}

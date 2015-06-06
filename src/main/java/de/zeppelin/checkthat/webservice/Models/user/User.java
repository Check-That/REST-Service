package de.zeppelin.checkthat.webservice.Models.user;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.zeppelin.checkthat.webservice.Models.user.circle.Circle;
import de.zeppelin.checkthat.webservice.persicetence.UserRepository;

@Entity(name = "user")
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	public Long authId = UUID.randomUUID().getMostSignificantBits();
	@OneToMany(mappedBy = "owner")
	@JsonIgnore
	public List<Circle> circles;
	@JoinTable(name = "friends")
	@JsonIgnore
	public List<User> friends;
	@Column(name = "image_path")
	public String imagePath = "";
	@NotNull
	public String name;
	@Embedded
	public Privacy privacy = new Privacy();
	public String pushToken = "";
	@Transient
	@JsonIgnore
	@Autowired
	private UserRepository userRep;

	protected User() {
	}

	public User(String name) {
		this.name = name;
	}
}

package de.zeppelin.checkthat.webservice.models.user;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import de.zeppelin.checkthat.webservice.models.JSONViews;
import de.zeppelin.checkthat.webservice.persisetence.UserRepository;

@Entity(name = "user")
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(JSONViews.nonSensitive.class)
	public Long id;
	@NotNull
	@JsonView(JSONViews.nonSensitive.class)
	public String name;
	@NotNull
	@JsonView(JSONViews.sensitive.class)
	public String language;
	@JsonView(JSONViews.sensitive.class)
	public String email;
	@JsonView(JSONViews.sensitive.class)
	public String phone;
	@Column(name = "image_path")
	@JsonView(JSONViews.nonSensitive.class)
	public String image = "";
	@Embedded
	@JsonView(JSONViews.nonSensitive.class)
	public Privacy privacy = new Privacy();
	@JsonView(JSONViews.sensitive.class)
	public Long authId = UUID.randomUUID().getMostSignificantBits();
//	@ManyToMany(fetch = FetchType.LAZY)
//	@JsonIgnore
//	public List<Circle> circles;
	@JoinTable(name = "Friends")
	@JsonIgnore
	public List<User> friends;	
	public String pushToken = "";
	@Transient
	@JsonIgnore
	@Autowired
	private UserRepository userRep;

	protected User() {
	}

	public User(String name) {
		this.name = name;
		this.language = "de-de";
		this.email = "testmail.com";
		this.phone = "0166666";
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", language=" + language + ", email=" + email + ", phone=" + phone
				+ ", image=" + image + ", privacy=" + privacy + ", authId=" + authId + ", friends=" + friends
				+ ", pushToken=" + pushToken + "]";
	}
}

package de.zeppelin.checkthat.webservice.models.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.zeppelin.checkthat.webservice.models.user.circle.Circle;
import de.zeppelin.checkthat.webservice.persisetence.UserRepository;

@Entity(name = "user")
@Table(name = "user")
@JsonIgnoreProperties({"email","phone","authId","circles","friends","credits"})
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	@Column(nullable=false, unique=true)
	public String uniqueString;
	@Column(nullable=false)
	public String name;
	public String status = "*--*";
	@Temporal(TemporalType.TIMESTAMP)
	public Date lastActivity = new Date();
	public Integer credits = 0;
	public Integer createdSurveys = 0;
	public Integer respondedSurveys = 0;
	@Column(nullable = false)
	public String language;
	public String email;
	public String phone;
	@Column(name = "image_path")
	public String image = "";
	@Embedded
	public Privacy privacy = new Privacy();
	public Long authId;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
	public List<Circle> circles = new ArrayList<Circle>();
	@JoinTable(name = "Friends")
	public List<User> friends = new ArrayList<User>();
	@JsonIgnore
	public String pushToken;

	
	@Transient
	@JsonIgnore
	@Autowired
	private UserRepository userRep;

	protected User() {
	}

	public User(String name) {
		this.authId = UUID.randomUUID().getMostSignificantBits();
		this.name = name;
		this.language = "de-de";
		this.email = "testmail.com";
		this.phone = "0166666";
	}
	
	public User(Long id, String name, String language, String email, String phone, String image, Privacy privacy,
			Long authId, List<Circle> circles, List<User> friends, String pushToken) {
		super();
		this.id = id;
		this.name = name;
		this.language = language;
		this.email = email;
		this.phone = phone;
		this.image = image;
		this.privacy = privacy;
		this.authId = authId;
		this.circles = circles;
		this.friends = friends;
		this.pushToken = pushToken;
	}
	

	public User(Long id, String uniqueString, String name, String status, Date lastActivity, Integer credits,
			Integer createdSurveys, Integer respondedSurveys, String language, String email, String phone, String image,
			Privacy privacy, Long authId, List<Circle> circles, List<User> friends, String pushToken) {
		super();
		this.id = id;
		this.uniqueString = uniqueString;
		this.name = name;
		this.status = status;
		this.lastActivity = lastActivity;
		this.credits = credits;
		this.createdSurveys = createdSurveys;
		this.respondedSurveys = respondedSurveys;
		this.language = language;
		this.email = email;
		this.phone = phone;
		this.image = image;
		this.privacy = privacy;
		this.authId = authId;
		this.circles = circles;
		this.friends = friends;
		this.pushToken = pushToken;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", uniqueString=" + uniqueString + ", name=" + name + ", status=" + status
				+ ", lastActivity=" + lastActivity + ", credits=" + credits + ", createdSurveys=" + createdSurveys
				+ ", respondedSurveys=" + respondedSurveys + ", language=" + language + ", email=" + email + ", phone="
				+ phone + ", image=" + image + ", privacy=" + privacy + ", authId=" + authId + ", circles=" + circles
				+ ", friends=" + friends + ", pushToken=" + pushToken + "]";
	}
}

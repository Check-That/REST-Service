package de.zeppelin.checkthat.webservice.models.user;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.eclipse.persistence.annotations.PrivateOwned;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.zeppelin.checkthat.webservice.Application;
import de.zeppelin.checkthat.webservice.exceptions.InternalServerErrorException;
import de.zeppelin.checkthat.webservice.models.helper.ImageHelper;
import de.zeppelin.checkthat.webservice.models.image.ProfileImageType;
import de.zeppelin.checkthat.webservice.models.user.group.Group;
import de.zeppelin.checkthat.webservice.persisetence.UserRepository;

@Entity(name = "user")
@Table(name = "user")
@JsonIgnoreProperties({ "authId", "credits", "language", "email", "phone", "groups", "friends" })
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	@Column(nullable = false, unique = true)
	public String uniqueString;

	@Column(nullable = false)
	public String name;
	public String status = "*--*";
	public String image = "";
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "compare", column = @Column(name = "created_compare")),
			@AttributeOverride(name = "rate", column = @Column(name = "created_rate"))})
	public SurveyStatistic createdSurveys = new SurveyStatistic();
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "compare", column = @Column(name = "responded_compare")),
		@AttributeOverride(name = "rate", column = @Column(name = "responded_rate"))})
	public SurveyStatistic respondedSurveys = new SurveyStatistic();

	@Temporal(TemporalType.TIMESTAMP)
	public Date lastActivity = new Date();
	@Embedded
	public Privacy privacy = new Privacy();

	// RegistrationUser on iOS

	@Column(unique = true)
	public Long authId;
	public Integer credits = 0;
	@Column(nullable = false)
	public String language;
	public String email = "";
	public String phone = "";

	// Profile on iOS

	@PrivateOwned
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.ALL)
	public List<Group> groups = new ArrayList<Group>();
	@OneToMany
	@JoinTable(name = "friends", joinColumns = {
			@JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "friend_id", referencedColumnName = "id") })
	public List<User> friends = new ArrayList<User>();
	@JsonIgnore
	public String pushToken;

	@Transient
	@JsonIgnore
	@Autowired
	private UserRepository userRep;
	
	public User() {}

	public User(String name, String uniqueString) {
		this.authId = UUID.randomUUID().getMostSignificantBits();
		this.uniqueString = uniqueString;
		this.name = name;
		this.language = "de-de";
		this.email = "testmail.com";
		this.phone = "0166666";
	}

	public User(Long id, String name, String language, String email, String phone, Privacy privacy, Long authId,
			List<Group> circles, List<User> friends, String pushToken) {
		super();
		this.id = id;
		this.name = name;
		this.language = language;
		this.email = email;
		this.phone = phone;
		this.privacy = privacy;
		this.authId = authId;
		this.groups = circles;
		this.friends = friends;
		this.pushToken = pushToken;
	}

	public User(Long id, String uniqueString, String name, String status, String image, Date lastActivity, Integer credits,
			SurveyStatistic createdSurveys, SurveyStatistic respondedSurveys, String language, String email,
			String phone, Privacy privacy, Long authId, List<Group> circles, List<User> friends, String pushToken) {
		super();
		this.id = id;
		this.uniqueString = uniqueString;
		this.name = name;
		this.status = status;
		this.image = image;
		this.lastActivity = lastActivity;
		this.credits = credits;
		this.createdSurveys = createdSurveys;
		this.respondedSurveys = respondedSurveys;
		this.language = language;
		this.email = email;
		this.phone = phone;
		this.privacy = privacy;
		this.authId = authId;
		this.groups = circles;
		this.friends = friends;
		this.pushToken = pushToken;
	}

	@Async
	public void saveImage(MultipartFile mpFile) {
		this.image = UUID.randomUUID().toString();
		try {
			BufferedImage bImage = ImageIO.read(mpFile.getInputStream());
			for (ProfileImageType imageType : ProfileImageType.values()) {
				BufferedImage scaledImage = ImageHelper.scaleImage(bImage, imageType.width, imageType.height);
				
				ImageHelper.saveToAWS(scaledImage, Application.profileImagesBucketName, this.image + imageType.postfix + ".jpg");
			}
		} catch (Exception e) {
			throw new InternalServerErrorException();
		}
	}

	@JsonIgnore
	public String getImagePath() {
		return getImagePath("");
	}

	public String getImagePath(String postfix) {
		if (this.id == null || this.id == 0) {
			return null;
		}
		String rootDir = "C:\\checkthat\\img\\user";

		// Fill up id with zeros to get 12 digits
		String fileName = String.format("%012d", this.id);

		if (postfix != null || postfix != "") {
			postfix = "@" + postfix;
		}

		Path path = Paths.get(rootDir, fileName.substring(0, 3), fileName.substring(3, 6), fileName.substring(6, 9),
				fileName + postfix + ".jpg");

		return path.toString();
	}

	@Override
	public String toString() {
		return "User [id=" + this.id + ", uniqueString=" + this.uniqueString + ", name=" + this.name + ", status="
				+ this.status + ", createdSurveys=" + this.createdSurveys + ", respondedSurveys="
				+ this.respondedSurveys + ", lastActivity=" + this.lastActivity + ", privacy=" + this.privacy
				+ ", authId=" + this.authId + ", credits=" + this.credits + ", language=" + this.language + ", email="
				+ this.email + ", phone=" + this.phone + ", groups=" + this.groups + ", friends=" + this.friends
				+ ", pushToken=" + this.pushToken + "]";
	}

	public S3Object getImage(ProfileImageType imageType) {
		return ImageHelper.getImage(Application.profileImagesBucketName, this.image + imageType.postfix + ".jpg");		
	}
}

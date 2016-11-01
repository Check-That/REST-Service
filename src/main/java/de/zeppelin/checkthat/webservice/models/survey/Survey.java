package de.zeppelin.checkthat.webservice.models.survey;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.zeppelin.checkthat.webservice.models.image.Image;
import de.zeppelin.checkthat.webservice.models.response.Response;
import de.zeppelin.checkthat.webservice.models.user.User;

@Entity(name = "survey")
@Table(name = "survey")
public class Survey {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	public Boolean active = true;
	public String title;
	@Temporal(TemporalType.TIMESTAMP)
	public Date creationDate = new Date();
	@Temporal(TemporalType.TIMESTAMP)
	public Date expirationDate;
	@Enumerated(EnumType.STRING)
	public SurveyType type;
//	@OneToOne
	public User creator;
	public List<String> rateCategories;
	@Enumerated(EnumType.STRING)
	public SurveyPriority priority = SurveyPriority.Regular;
	@OneToMany(mappedBy = "survey")
	public List<Image> images = new ArrayList<Image>();
	@JoinTable(name = "participants", joinColumns = {
			@JoinColumn(name = "survey_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "user_id", referencedColumnName = "id") })
	public List<User> participants = new ArrayList<User>();
	@OneToMany(mappedBy = "survey")
	public List<Response> responses = new ArrayList<Response>();

	public Survey() {
	}

	public Survey(User creator, List<Image> images, String title, List<String> categories, SurveyType type,
			List<User> participants) {
		super();
		this.creator = creator;
		this.images = images;
		this.title = title;
		this.rateCategories = categories;
		this.type = type;
		this.participants = participants;
	}

	@Override
	public String toString() {
		return "Survey [id=" + this.id + ", title=" + this.title + ", expirationDate=" + this.expirationDate + ", type=" + this.type
				+ ", creator=" + this.creator + ", rateCategories=" + this.rateCategories + ", priority="
				+ this.priority + ", images=" + this.images + ", participants=" + this.participants + ", responses="
				+ this.responses + "]";
	}

}
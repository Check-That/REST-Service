package de.zeppelin.checkthat.webservice.Models.survey;

import java.util.ArrayList;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

import de.zeppelin.checkthat.webservice.Models.Views.Flatsurvey;
import de.zeppelin.checkthat.webservice.Models.answer.Answer;
import de.zeppelin.checkthat.webservice.Models.user.User;

@Entity(name = "survey")
@Table(name = "survey")
public class Survey {
	public Survey(User creator, String image, String title,
			List<String> categories, SurveyType type, List<User> participants) {
		super();
		this.creator = creator;
		this.image = image;
		this.title = title;
		this.categories = categories;
		this.type = type;
		this.participants = participants;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	@OneToOne
	@JsonView(Flatsurvey.class)
	public User creator;
	public String image = "";
	public String title = "";
	public List<String> categories = new ArrayList<String>();
	@Enumerated(EnumType.STRING)
	public SurveyType type = SurveyType.Choose;
	@JoinTable(name = "participants", joinColumns = { @JoinColumn(name = "survey_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") })
	public List<User> participants = new ArrayList<User>();
	@OneToMany(mappedBy = "survey")
	public List<Answer> answers = new ArrayList<Answer>();
	
	public Survey() {
	}

}
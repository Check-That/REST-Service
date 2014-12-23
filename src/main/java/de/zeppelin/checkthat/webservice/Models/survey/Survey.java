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
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	@OneToOne
	@JsonView(Flatsurvey.class)
	public User creator;
	public String image = "";
	public String title = "";
	@Enumerated(EnumType.STRING)
	public SurveyType type = SurveyType.Choose;
	@JoinTable(name = "participants", joinColumns = { @JoinColumn(name = "survey_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") })
	public List<User> participants = new ArrayList<User>();
	@OneToMany(mappedBy = "survey")
	public List<Answer> answers = new ArrayList<Answer>();

	public Survey() {
	}

	public Survey(User creator, SurveyType type, String title, String image,
			List<User> participants) {
		this.creator = creator;
		this.type = type;
		this.title = title;
		this.image = image;
		this.participants = participants;
	}
}
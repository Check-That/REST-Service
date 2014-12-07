package de.zeppelin.checkthat.webservice.Models.survey;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import de.zeppelin.checkthat.webservice.Models.answer.Answer;
import de.zeppelin.checkthat.webservice.Models.user.User;

@Entity(name = "survey")
@Table(name = "survey")
public class Survey {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	@OneToOne
	public User creator;
	public String image;
	public String title;
	@Enumerated(EnumType.STRING)
	public SurveyType type;
	public List<User> participants;
	public List<Answer> answers;

	public Survey() {
	}

	public Survey(String title, String image) {
		this.title = title;
		this.image = image;
	}
}
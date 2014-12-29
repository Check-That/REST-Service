package de.zeppelin.checkthat.webservice.Models.answer;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.zeppelin.checkthat.webservice.Models.survey.Survey;
import de.zeppelin.checkthat.webservice.Models.user.User;

@Entity(name = "answer")
@Table(name = "answer")
public class Answer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	public User owner;
	@ManyToOne
	public Survey survey;
	public List<String> answerValues;

	public Answer() {
	}
}

package de.zeppelin.checkthat.webservice.Models.answer;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import de.zeppelin.checkthat.webservice.Models.survey.Survey;
import de.zeppelin.checkthat.webservice.Models.survey.SurveyType;
import de.zeppelin.checkthat.webservice.Models.user.User;

@Entity(name="answer")
@Table(name="answer")
public class Answer { //N.I.N.J.A
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	@OneToOne
	public User owner;
	public Survey survey;
	public String answer;
	
	public Answer(){};//N.I.N.J.A
}

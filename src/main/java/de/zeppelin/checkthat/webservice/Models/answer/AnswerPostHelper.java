package de.zeppelin.checkthat.webservice.Models.answer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import de.zeppelin.checkthat.webservice.Application;
import de.zeppelin.checkthat.webservice.persicetence.AnswerRepository;
import de.zeppelin.checkthat.webservice.persicetence.SurveyRepository;
import de.zeppelin.checkthat.webservice.persicetence.UserRepository;

@Configurable(autowire = Autowire.BY_TYPE)
@Service
public class AnswerPostHelper {
	public List<Integer> answerValues;
	public Long ownerId;

	public AnswerPostHelper() {
	}

	public Answer generateAnswer(Long surveyID) {
		UserRepository userRep = Application.getContext().getBean(
				UserRepository.class);
		SurveyRepository surveyRep = Application.getContext().getBean(
				SurveyRepository.class);
		AnswerRepository answerRep = Application.getContext().getBean(
				AnswerRepository.class);

		Answer answer = new Answer();
		answer.survey = surveyRep.findOne(surveyID);
		answer.answerValues = this.answerValues;
		answer.owner = userRep.findOne(this.ownerId);

		answerRep.save(answer);
		return answer;
	}
}

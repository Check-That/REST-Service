package de.zeppelin.checkthat.webservice.persicetence;

import org.springframework.data.repository.CrudRepository;

import de.zeppelin.checkthat.webservice.Models.survey.Survey;
import de.zeppelin.checkthat.webservice.Models.user.User;

public interface SurveyRepository extends CrudRepository<Survey, Long> {
	Iterable<Survey> findByCreator(User user);
	Iterable<Survey> findByParticipants(User user);
}

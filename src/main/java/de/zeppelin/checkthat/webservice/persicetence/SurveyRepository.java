package de.zeppelin.checkthat.webservice.persicetence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.zeppelin.checkthat.webservice.Models.survey.Survey;
import de.zeppelin.checkthat.webservice.Models.user.User;

@Repository
public interface SurveyRepository extends CrudRepository<Survey, Long> {
	Iterable<Survey> findByCreator(User user);

	Iterable<Survey> findByParticipants(User user);
}

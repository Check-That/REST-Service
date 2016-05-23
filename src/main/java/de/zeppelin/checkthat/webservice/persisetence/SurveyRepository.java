package de.zeppelin.checkthat.webservice.persisetence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.zeppelin.checkthat.webservice.models.image.Image;
import de.zeppelin.checkthat.webservice.models.survey.Survey;
import de.zeppelin.checkthat.webservice.models.user.User;


@Repository
public interface SurveyRepository extends CrudRepository<Survey, Long> {
	Iterable<Survey> findByCreator(User user);
	Iterable<Survey> findByCreatorAuthId(Long authId);
	
	@Query("SELECT s FROM survey s, user u WHERE u.authId = :authId AND u MEMBER OF s.participants AND s NOT IN (SELECT r.survey FROM response r WHERE r.responder.authId = :authId)")
	Iterable<Survey> findInbox(@Param("authId") Long authId);
	Iterable<Survey> findByParticipants(User user);
	@Query("SELECT r.survey FROM response r WHERE r.responder.authId = :authId")
	Iterable<Survey> findResponded(@Param("authId") Long authId);
}

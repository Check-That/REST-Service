package de.zeppelin.checkthat.webservice.persisetence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.zeppelin.checkthat.webservice.models.survey.Survey;
import de.zeppelin.checkthat.webservice.models.user.User;

@Repository
public interface SurveyRepository extends CrudRepository<Survey, Long> {
	Iterable<Survey> findByCreator(User user);

	Iterable<Survey> findByCreatorAuthId(Long authId);

	@Query("SELECT s from survey s WHERE s.creator.authId = :authId AND s.expirationDate > CURRENT_DATE AND SIZE(s.responses) < SIZE(s.participants)")
	Iterable<Survey> findOwnPending(@Param("authId") Long authId);

	@Query("SELECT s from survey s WHERE s.creator.authId = :authId AND (s.expirationDate <= CURRENT_DATE OR SIZE(s.responses) >= SIZE(s.participants))")
	Iterable<Survey> findOwnCompleted(@Param("authId") Long authId);

	@Query("SELECT s FROM survey s, user u WHERE u.authId = :authId AND u MEMBER OF s.participants AND s NOT IN (SELECT r.survey FROM response r WHERE r.responder.authId = :authId)")
	Iterable<Survey> findInbox(@Param("authId") Long authId);

	Iterable<Survey> findByParticipants(User user);

	@Query("SELECT r.survey FROM response r, survey s WHERE r.responder.authId = :authId AND s.id = r.survey.id AND r.favorite = true")
	Iterable<Survey> findRespondedFavorites(@Param("authId") Long authId);

	@Query("SELECT r.survey FROM response r, survey s WHERE r.responder.authId = :authId AND s.id = r.survey.id AND r.favorite = false")
	Iterable<Survey> findRespondedNonFavorites(@Param("authId") Long authId);

	// @Query("SELECT r.survey FROM response r, survey s WHERE
	// r.responder.authId = :authId AND s.id = r.survey.id AND s.expirationDate
	// > CURRENT_DATE AND SIZE(s.responses) < SIZE(s.participants)")
	// Iterable<Survey> findRespondedPending(@Param("authId") Long authId);
	//
	// @Query("SELECT r.survey FROM response r, survey s WHERE
	// r.responder.authId = :authId AND s.id = r.survey.id AND (s.expirationDate
	// <= CURRENT_DATE OR SIZE(s.responses) >= SIZE(s.participants))")
	// Iterable<Survey> findRespondedCompleted(@Param("authId") Long authId);
}

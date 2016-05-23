package de.zeppelin.checkthat.webservice.persisetence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.zeppelin.checkthat.webservice.models.response.Response;

@Repository
public interface ResponseRepository extends CrudRepository<Response, Long> {
	Response findOneBySurveyIdAndResponderAuthId(Long surveyId, Long authId);
}

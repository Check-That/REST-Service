package de.zeppelin.checkthat.webservice.persicetence;

import org.springframework.data.repository.CrudRepository;

import de.zeppelin.checkthat.webservice.Models.vote.Survey;

public interface SurveyRepository extends CrudRepository<Survey, Long> {

}

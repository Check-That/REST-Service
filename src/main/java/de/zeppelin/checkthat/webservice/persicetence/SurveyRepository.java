package de.zeppelin.checkthat.webservice.persicetence;

import org.springframework.data.repository.CrudRepository;

import de.zeppelin.checkthat.webservice.Models.survey.Survey;

public interface SurveyRepository extends CrudRepository<Survey, Long> {

}

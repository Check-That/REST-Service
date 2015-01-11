package de.zeppelin.checkthat.webservice.persicetence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.zeppelin.checkthat.webservice.Models.answer.Answer;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Long> {

}

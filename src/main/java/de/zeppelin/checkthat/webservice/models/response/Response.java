package de.zeppelin.checkthat.webservice.models.response;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.zeppelin.checkthat.webservice.models.survey.Survey;
import de.zeppelin.checkthat.webservice.models.user.User;

@Entity(name = "response")
@Table(name = "response")
@JsonIgnoreProperties(value = { "survey" })
public class Response {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	@Temporal(TemporalType.TIMESTAMP)
	public Date date = new Date();
	@JsonIgnore
	public Boolean favorite;
	public List<Integer> responseValues;
	public User responder;
	@ManyToOne
	public Survey survey;

	public Response() {
	}
}
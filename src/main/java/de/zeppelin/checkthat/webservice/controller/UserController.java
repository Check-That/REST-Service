package de.zeppelin.checkthat.webservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import de.zeppelin.checkthat.webservice.exceptions.BadRequestException;
import de.zeppelin.checkthat.webservice.exceptions.ForbiddenException;
import de.zeppelin.checkthat.webservice.models.JSONViews;
import de.zeppelin.checkthat.webservice.models.survey.Survey;
import de.zeppelin.checkthat.webservice.models.user.User;
import de.zeppelin.checkthat.webservice.persisetence.SurveyRepository;
import de.zeppelin.checkthat.webservice.persisetence.UserRepository;

@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	UserRepository userRep;

	@Autowired
	SurveyRepository surveyRep;

	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Iterable<User> getAll() {
		return this.userRep.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	@JsonView(JSONViews.sensitive.class)
	public User newUser(@RequestBody User user) {
		if (user != null) {
			if (user.name != null && !user.name.isEmpty()) {
				User newUser = this.userRep.save(user);
				return newUser;
			} else if (user.authId != null && user.authId != 0){
				User regUser = this.userRep.findOneByAuthId(user.authId);
				if (regUser != null) return regUser;
				throw new ForbiddenException();
			} 
			throw new BadRequestException();
		}
		throw new BadRequestException();
	}

	@RequestMapping("{id}")
	@JsonView(JSONViews.sensitive.class)
	public User getUserById(@PathVariable("id") String id) {
		return this.userRep.findOne(Long.parseLong(id));
	}

	@RequestMapping("{id}/created")
	public Iterable<Survey> getCreatedSurveysByUser(@PathVariable("id") Long id) {
		return this.surveyRep.findByCreator(this.userRep.findOne(id));
	}

	@RequestMapping("{id}/participating")
	public Iterable<Survey> getParticipatingSurveysByUser(
			@PathVariable("id") Long id) {
		return this.surveyRep.findByParticipants(this.userRep.findOne(id));
	}
}
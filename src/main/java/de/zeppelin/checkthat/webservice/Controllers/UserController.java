package de.zeppelin.checkthat.webservice.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.zeppelin.checkthat.webservice.Models.survey.Survey;
import de.zeppelin.checkthat.webservice.Models.user.Privacy;
import de.zeppelin.checkthat.webservice.Models.user.User;
import de.zeppelin.checkthat.webservice.persicetence.SurveyRepository;
import de.zeppelin.checkthat.webservice.persicetence.UserRepository;

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
	public Long newUser(@RequestBody User user) {
		if (user != null && user.name != null && !user.name.isEmpty()) {
			user.privacy = new Privacy();
			User newUser = this.userRep.save(user);
			return newUser.authId;
		} else {
			return null;
		}
	}

	@RequestMapping("{id}")
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
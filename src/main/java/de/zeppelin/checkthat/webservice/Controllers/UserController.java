package de.zeppelin.checkthat.webservice.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;

import de.zeppelin.checkthat.webservice.Models.Views.Flatsurvey;
import de.zeppelin.checkthat.webservice.Models.survey.Survey;
import de.zeppelin.checkthat.webservice.Models.user.Privacy;
import de.zeppelin.checkthat.webservice.Models.user.User;
import de.zeppelin.checkthat.webservice.persicetence.SurveyRepository;
import de.zeppelin.checkthat.webservice.persicetence.UserRepository;

@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	UserRepository userRep;
	
	@Autowired
	SurveyRepository surveyRep;

	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public Iterable<User> getAll() {
		return this.userRep.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public User newUser(@RequestBody User user) {
		if (user != null && user.name != null && !user.name.isEmpty()) {
			user.privacy = new Privacy();
			User newUser = this.userRep.save(user);
			return newUser;
		} else {
			return null;
		}
	}

	@RequestMapping("{id}")
	@ResponseBody
	public User getUserById(@PathVariable("id") String id) {
		return this.userRep.findOne(Long.parseLong(id));
	}
	
	@RequestMapping("{id}/created")
	@ResponseBody
	@JsonView(Flatsurvey.class)
	public Iterable<Survey> getCreatedSurveysByUser(@PathVariable("id") Long id) {
		return this.surveyRep.findByCreator(userRep.findOne(id));
	}
	
	@RequestMapping("{id}/participating")
	@ResponseBody
	@JsonView(Flatsurvey.class)
	public Iterable<Survey> getParticipatingSurveysByUser(@PathVariable("id") Long id) {
		return this.surveyRep.findByParticipants(userRep.findOne(id));
	}

	@RequestMapping("init")
	@ResponseBody
	public String initUsers() {
		this.userRep.save(new User("Yannick"));
		this.userRep.save(new User("Cedric"));

		return "ok";
	}
}
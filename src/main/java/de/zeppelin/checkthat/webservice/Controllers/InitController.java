package de.zeppelin.checkthat.webservice.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.zeppelin.checkthat.webservice.Models.survey.Survey;
import de.zeppelin.checkthat.webservice.Models.survey.SurveyType;
import de.zeppelin.checkthat.webservice.Models.user.User;
import de.zeppelin.checkthat.webservice.persicetence.SurveyRepository;
import de.zeppelin.checkthat.webservice.persicetence.UserRepository;

@Controller
@RequestMapping("init")
public class InitController {

	@Autowired
	UserRepository userRep;
	@Autowired
	SurveyRepository surveyRep;

	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public String initGeneral() {
		User cedric = new User("Cedric");
		User yannick = new User("Yannick");
		User horst = new User("Horst");
		this.userRep.save(cedric);
		this.userRep.save(yannick);
		this.userRep.save(horst);

		cedric = this.userRep.findOne(new Long("1"));
		yannick = this.userRep.findOne(new Long("2"));
		horst = this.userRep.findOne(new Long("3"));

		List<User> participants = new ArrayList<User>();
		participants.add(horst);
		participants.add(yannick);

		List<String> categories = new ArrayList<String>();
		categories.add("face");
		categories.add("beauty");

		Survey shoes = new Survey(cedric, "testimage", "shoes", categories,
				SurveyType.TopFlop, participants);

		Survey watch = new Survey(cedric, "testimage", "watch", categories,
				SurveyType.Choose, participants);

		Survey watch2 = new Survey(cedric, "testimage", "watches", categories,
				SurveyType.Stars, participants);

		this.surveyRep.save(watch2);
		this.surveyRep.save(watch);
		this.surveyRep.save(shoes);

		return "ok";
	}
}

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
//		 User cedric = new User("Cedric");
//		 User yannick = new User("Yannick");
//		 User horst = new User("Horst");

		User cedric = this.userRep.findOne(new Long("1"));
		User yannick = this.userRep.findOne(new Long("2"));
		User horst = this.userRep.findOne(new Long("3"));
		System.out.println(cedric);

		List<User> participants = new ArrayList<User>();
		participants.add(horst);
		participants.add(yannick);

		Survey shoes = new Survey(cedric, SurveyType.Choose, "shoes",
				"testimage", participants);

		// Survey watch = new Survey("Neue Uhr", "UhrImage");
		// watch.type = SurveyType.TopFlop;
		// watch.creator = yannick;
		// watch.participants.add(cedric);
		// watch.participants.add(horst);

//		 this.userRep.save(cedric);
//		 this.userRep.save(yannick);
//		 this.userRep.save(horst);
		// this.surveyRep.save(watch);
		this.surveyRep.save(shoes);

		return "ok";
	}
}

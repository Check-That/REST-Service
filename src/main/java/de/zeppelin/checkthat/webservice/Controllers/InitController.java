package de.zeppelin.checkthat.webservice.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
	SurveyRepository surveyRep;
	
	public String initGeneral() {
		User cedric = new User("Cedric");
		User yannick = new User("Yannick");
		User horst = new User("Horst");
		
		Survey shoes = new Survey("Coole Schuhe", "testImage");
		shoes.creator = cedric;
		shoes.type = SurveyType.Choose;
		shoes.participants.add(horst);
		shoes.participants.add(yannick);
		
		Survey watch = new Survey("Neue Uhr", "UhrImage");
		watch.type = SurveyType.TopFlop;
		watch.creator = yannick;
		watch.participants.add(cedric);
		watch.participants.add(horst);
		
		this.userRep.save(cedric);
		this.userRep.save(yannick);
		this.userRep.save(horst);
		this.surveyRep.save(watch);
		this.surveyRep.save(shoes);
		
		return "ok";
	}
}

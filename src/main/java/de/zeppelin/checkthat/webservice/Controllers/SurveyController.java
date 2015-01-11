package de.zeppelin.checkthat.webservice.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.zeppelin.checkthat.webservice.Models.answer.AnswerPostHelper;
import de.zeppelin.checkthat.webservice.Models.survey.Survey;
import de.zeppelin.checkthat.webservice.Models.survey.SurveyPostHelper;
import de.zeppelin.checkthat.webservice.persicetence.AnswerRepository;
import de.zeppelin.checkthat.webservice.persicetence.SurveyRepository;
import de.zeppelin.checkthat.webservice.persicetence.UserRepository;

@Controller
@RequestMapping("survey")
public class SurveyController {

	@Autowired
	SurveyRepository surveyRep;
	@Autowired
	UserRepository userRep;
	@Autowired
	AnswerRepository answerRep;

	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public Iterable<Survey> getAll() {
		return this.surveyRep.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public String createSurvey(@RequestBody SurveyPostHelper helper) {
		try {
			helper.generateSurvey();
		} catch (Exception e) {
			return "error";
		}
		return "ok";
	}

	/* Später aus Sicherheitsgründen nicht mehr verwenden */
	@Deprecated
	@RequestMapping("{id}")
	@ResponseBody
	public Survey getSurveyById(@PathVariable("id") String id) {
		return this.surveyRep.findOne(Long.parseLong(id));
	}

	@RequestMapping(value = "{surveyID}", method = RequestMethod.POST)
	@ResponseBody
	public String postAnswerforSurvey(@PathVariable("surveyID") Long surveyID,
			@RequestBody AnswerPostHelper helper) {
		try {
			helper.generateAnswer(surveyID);
		} catch (Exception e) {
			return "error";
		}
		return "ok";
	}
}
package de.zeppelin.checkthat.webservice.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.zeppelin.checkthat.webservice.Models.survey.Survey;
import de.zeppelin.checkthat.webservice.persicetence.SurveyRepository;

@Controller
@RequestMapping("survey")
public class SurveyController {

	@Autowired
	SurveyRepository repository;

	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public Iterable<Survey> getAll() {
		return this.repository.findAll();
	}

	@RequestMapping("{id}")
	@ResponseBody
	public Survey getSurveyById(@PathVariable("id") String id) {
		return this.repository.findOne(Long.parseLong(id));
	}

	@RequestMapping("init")
	@ResponseBody
	public String init() {
		this.repository
				.save(new Survey(null, null, "Cedric", "testimage", null));
		return "ok";
	}
}

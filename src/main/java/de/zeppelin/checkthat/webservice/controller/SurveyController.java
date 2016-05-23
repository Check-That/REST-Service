package de.zeppelin.checkthat.webservice.controller;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import de.zeppelin.checkthat.webservice.exceptions.BadRequestException;
import de.zeppelin.checkthat.webservice.exceptions.ConflictException;
import de.zeppelin.checkthat.webservice.exceptions.ForbiddenException;
import de.zeppelin.checkthat.webservice.exceptions.UnauthorizedException;
import de.zeppelin.checkthat.webservice.models.image.Image;
import de.zeppelin.checkthat.webservice.models.response.ResponsePostHelper;
import de.zeppelin.checkthat.webservice.models.survey.Survey;
import de.zeppelin.checkthat.webservice.models.survey.SurveyPostHelper;
import de.zeppelin.checkthat.webservice.persisetence.ImageRepository;
import de.zeppelin.checkthat.webservice.persisetence.ResponseRepository;
import de.zeppelin.checkthat.webservice.persisetence.SurveyRepository;
import de.zeppelin.checkthat.webservice.persisetence.UserRepository;
import jersey.repackaged.com.google.common.collect.Lists;

@Controller
@RequestMapping("survey")
public class SurveyController {

	@Autowired
	SurveyRepository surveyRep;
	@Autowired
	UserRepository userRep;
	@Autowired
	ResponseRepository answerRep;
	@Autowired
	ImageRepository imageRepository;

	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public Map<String, Iterable<Survey>> getUserSurveys(@RequestHeader(value = "authId", required = false) Long authId) {
		if (authId == null) throw new UnauthorizedException();
		if (this.userRep.countByauthId(authId) < 1) throw new ForbiddenException();

		HashMap<String, Iterable<Survey>> map = new HashMap<String, Iterable<Survey>>();
		map.put("own", this.surveyRep.findByCreatorAuthId(authId));
		map.put("inbox", this.surveyRep.findInbox(authId));
		map.put("responded", this.surveyRep.findResponded(authId));
		return map;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Survey createSurvey(@RequestBody SurveyPostHelper helper, @RequestHeader(value = "authId", required = false) Long authId) {
		if (authId == null || authId == 0) throw new BadRequestException();
		return helper.generateSurvey(authId);
	}

	/* Später aus Sicherheitsgründen nicht mehr verwenden */
	@Deprecated
	@RequestMapping("{id}")
	@ResponseBody
	public Survey getSurveyById(@PathVariable("id") String id) {
		return this.surveyRep.findOne(Long.parseLong(id));
	}

	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public String removeSurveyById(@PathVariable("id") String id) {
		try {
			this.surveyRep.delete(Long.parseLong(id));
		} catch (Exception e) {
			return "error";
		}
		return "ok";
	}

	@RequestMapping(value = "{surveyId}", method = RequestMethod.POST)
	@ResponseBody
	public Survey postAnswerForSurvey(@PathVariable("surveyId") Long surveyId, @RequestHeader(value = "authId", required = false) Long authId, @RequestBody ResponsePostHelper helper) {
		return helper.generateAnswer(surveyId, authId).survey;
	}

	@RequestMapping(value = "{surveyId}/images", method = RequestMethod.GET)
	public String getFileUpload() {
		return "imageUpload";
	}
}
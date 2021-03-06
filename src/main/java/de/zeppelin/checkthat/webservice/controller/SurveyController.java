package de.zeppelin.checkthat.webservice.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;

import de.zeppelin.checkthat.webservice.exceptions.ConflictException;
import de.zeppelin.checkthat.webservice.exceptions.ForbiddenException;
import de.zeppelin.checkthat.webservice.models.JSONViews;
import de.zeppelin.checkthat.webservice.models.helper.ResponsePostHelper;
import de.zeppelin.checkthat.webservice.models.helper.SurveyPostHelper;
import de.zeppelin.checkthat.webservice.models.helper.UserHelper;
import de.zeppelin.checkthat.webservice.models.survey.Survey;
import de.zeppelin.checkthat.webservice.models.survey.SurveySection;
import de.zeppelin.checkthat.webservice.persisetence.ImageRepository;
import de.zeppelin.checkthat.webservice.persisetence.ResponseRepository;
import de.zeppelin.checkthat.webservice.persisetence.SurveyRepository;
import de.zeppelin.checkthat.webservice.persisetence.UserRepository;

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
	
	@Autowired
	UserHelper userHelper;

	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@JsonView(JSONViews.nonSensitive.class)
	public Map<String, List<SurveySection>> getUserSurveys(
			@RequestHeader(value = "authId", required = false) Long authId) {

		userHelper.checkAuthId(authId);

		HashMap<String, List<SurveySection>> map = new HashMap<String, List<SurveySection>>();

		List<SurveySection> surveySectionsInbox = new ArrayList<SurveySection>();
		surveySectionsInbox.add(new SurveySection("friends", this.surveyRep.findInbox(authId)));
		surveySectionsInbox.add(new SurveySection("suggestions", getSuggestedSurveys()));
		map.put("inbox", surveySectionsInbox);

		List<SurveySection> surveySectionsResponded = new ArrayList<SurveySection>();
		surveySectionsResponded.add(new SurveySection("favorites", this.surveyRep.findRespondedFavorites(authId)));
		surveySectionsResponded.add(new SurveySection("others", this.surveyRep.findRespondedNonFavorites(authId)));
		map.put("responded", surveySectionsResponded);

		List<SurveySection> surveySectionsOwn = new ArrayList<SurveySection>();
		surveySectionsOwn.add(new SurveySection("pending", this.surveyRep.findOwnPending(authId)));
		surveySectionsOwn.add(new SurveySection("completed", this.surveyRep.findOwnCompleted(authId)));
		map.put("own", surveySectionsOwn);

		return map;
	}

	private Iterable<Survey> getSuggestedSurveys() {
		// TODO Suggestions generieren

		return new ArrayList<Survey>();
	}

	@RequestMapping(method = RequestMethod.POST)
	@JsonView(JSONViews.nonSensitive.class)
	@ResponseBody
	public Survey createSurvey(@RequestPart(required = true) SurveyPostHelper helper,
			@RequestHeader(value = "authId", required = false) Long authId,
			@RequestPart(value = "file", required = true) final MultipartFile[] images) {
		userHelper.checkAuthId(authId);
		return helper.generateSurvey(authId, images);
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
	public String removeSurveyById(@PathVariable("id") Long id,
			@RequestHeader(value = "authId", required = false) Long authId) {
		userHelper.checkAuthId(authId);

		Survey survey = this.surveyRep.findOne(id);
		if (survey == null) {
			throw new ConflictException();
		}
		if (survey.creator.authId != authId) {
			throw new ForbiddenException();
		}

		survey.active = false;
		this.surveyRep.save(survey);

		return "ok";
	}

	@RequestMapping(value = "{surveyId}", method = RequestMethod.POST)
	@JsonView(JSONViews.nonSensitive.class)
	@ResponseBody
	public Survey postAnswerForSurvey(@PathVariable("surveyId") Long surveyId,
			@RequestHeader(value = "authId", required = false) Long authId, @RequestBody ResponsePostHelper helper) {
		userHelper.checkAuthId(authId);
		return helper.generateAnswer(surveyId, authId).survey;
	}

	@RequestMapping(value = "{surveyId}/images", method = RequestMethod.GET)
	public String getFileUpload() {
		return "imageUpload";
	}
}
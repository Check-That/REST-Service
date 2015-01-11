package de.zeppelin.checkthat.webservice.Models.survey;

import java.util.List;

import com.google.common.collect.Lists;

import de.zeppelin.checkthat.webservice.Application;
import de.zeppelin.checkthat.webservice.persicetence.SurveyRepository;
import de.zeppelin.checkthat.webservice.persicetence.UserRepository;

public class SurveyPostHelper {
	public Long creatorID;
	public String title;
	public List<String> categories;
	public SurveyType type;
	public List<Long> participants;
	public String image;

	public SurveyPostHelper() {
	}

	public Survey generateSurvey() {
		UserRepository userRep = Application.getContext().getBean(
				UserRepository.class);
		SurveyRepository surveyRep = Application.getContext().getBean(
				SurveyRepository.class);

		Survey survey = new Survey();
		survey.creator = userRep.findOne(this.creatorID);
		survey.title = this.title;
		survey.categories = this.categories;
		survey.type = this.type;
		survey.image = this.image;
		survey.participants = Lists.newArrayList(userRep
				.findAll(this.participants));

		surveyRep.save(survey);

		return survey;
	}
}
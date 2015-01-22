package de.zeppelin.checkthat.webservice.Models.survey;

import java.util.List;

import de.zeppelin.checkthat.webservice.Application;
import de.zeppelin.checkthat.webservice.persicetence.SurveyRepository;
import de.zeppelin.checkthat.webservice.persicetence.UserRepository;

public class SurveyPostHelper {
	public List<String> categories;
	public Long creatorID;
	public String image;
	public List<Long> participants;
	public String title;
	public SurveyType type;

	public SurveyPostHelper() {
	}

	public Survey generateSurvey() {
		UserRepository userRep = Application.getContext().getBean(
				UserRepository.class);
		SurveyRepository surveyRep = Application.getContext().getBean(
				SurveyRepository.class);

		Survey survey = new Survey();
		survey.categories = this.categories;
		survey.creator = userRep.findOne(this.creatorID);
		survey.image = this.image;
		for (Long participant : this.participants) {
			survey.participants.add(userRep.findOne(participant));
		}
		survey.title = this.title;
		survey.type = this.type;

		surveyRep.save(survey);

		return survey;
	}
}
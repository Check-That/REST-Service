package de.zeppelin.checkthat.webservice.models.survey;

import java.util.List;

import de.zeppelin.checkthat.webservice.Application;
import de.zeppelin.checkthat.webservice.exceptions.BadRequestException;
import de.zeppelin.checkthat.webservice.exceptions.ConflictException;
import de.zeppelin.checkthat.webservice.models.image.Image;
import de.zeppelin.checkthat.webservice.persisetence.ImageRepository;
import de.zeppelin.checkthat.webservice.persisetence.SurveyRepository;
import de.zeppelin.checkthat.webservice.persisetence.UserRepository;

public class SurveyPostHelper {
	public String title;
	public SurveyType type;
	public List<String> rateCategories;
	public SurveyPriority priority;
	public List<Long> participants;
	public List<String> images;
	
	private UserRepository userRep = Application.getContext().getBean(
			UserRepository.class);
	private SurveyRepository surveyRep = Application.getContext().getBean(
			SurveyRepository.class);
//	private ImageRepository imageRep = Application.getContext().getBean(
//			ImageRepository.class);

 	public SurveyPostHelper() {
	}

	public Survey generateSurvey(Long authId) {
		
		if (!this.checkValid()) throw new BadRequestException();

		Survey survey = new Survey();
		survey.rateCategories = this.rateCategories;
		survey.title = this.title;
		survey.type = this.type;
		
		try {
			survey.creator = userRep.findOneByAuthId(authId);
			for (Long participant : this.participants) {
				survey.participants.add(userRep.findOne(participant));
			}
			
			surveyRep.save(survey);
			
			for (String imageTitle : this.images) {
				ImageRepository imageRep = Application.getContext().getBean(
						ImageRepository.class);
				Image image = new Image();
				image.title = imageTitle;
				image.survey = survey;
				survey.images.add(imageRep.save(image));
			}
		} catch (Exception e) {
			throw new ConflictException();
		}

		return survey;
	}

	private boolean checkValid() {
		if (title == null  || title.length() < 3 || title.length() > 15) return false;
		if (type == null) return false;
		if (rateCategories == null || rateCategories.size() > 3 || rateCategories.size() < 1) return false;
		if (priority == null) return false; //Todo: Check when Sponsored || Paid  the pay-Authority So that everybody has paid correctly
		if (participants == null || participants.size() <= 1 || participants.size() > 20) return false; // Todo: Check if not the only participant is the creator
		return true;
	}

	@Override
	public String toString() {
		return "SurveyPostHelper [title=" + title + ", type=" + type + ", rateCategories=" + rateCategories
				+ ", priority=" + priority + ", participants=" + participants + "]";
	}
}
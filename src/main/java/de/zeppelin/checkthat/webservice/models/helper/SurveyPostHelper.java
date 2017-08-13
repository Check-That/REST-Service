package de.zeppelin.checkthat.webservice.models.helper;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.web.multipart.MultipartFile;

import de.zeppelin.checkthat.webservice.Application;
import de.zeppelin.checkthat.webservice.exceptions.BadRequestException;
import de.zeppelin.checkthat.webservice.exceptions.ConflictException;
import de.zeppelin.checkthat.webservice.models.image.Image;
import de.zeppelin.checkthat.webservice.models.survey.Survey;
import de.zeppelin.checkthat.webservice.models.survey.SurveyPriority;
import de.zeppelin.checkthat.webservice.models.survey.SurveyType;
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
	public int duration;

	private UserRepository userRep = Application.getContext().getBean(UserRepository.class);
	private SurveyRepository surveyRep = Application.getContext().getBean(SurveyRepository.class);
	private ImageRepository imageRep = Application.getContext().getBean(ImageRepository.class);

	private Long tmpAuthID;
	private MultipartFile[] tmpFiles;

	public SurveyPostHelper() {
	}

	public Survey generateSurvey(Long authId, MultipartFile[] files) {
		this.tmpAuthID = authId;
		this.tmpFiles = files;

		if (!checkValid()) {
			throw new BadRequestException();
		}

		Survey survey = new Survey();
		survey.rateCategories = this.rateCategories;
		survey.title = this.title;
		survey.type = this.type;
		survey.expirationDate = DateTime.now().plusSeconds(this.duration).toDate();

		try {
			survey.creator = this.userRep.findOneByAuthId(authId);
			for (Long participant : this.participants) {
				survey.participants.add(this.userRep.findOne(participant));
			}

			this.surveyRep.save(survey);

			for (int i = 0; i < this.images.size(); i++) {
				Image image = new Image();
				image.title = this.images.get(i);
				image.survey = survey;
				survey.images.add(this.imageRep.save(image));
				image.saveImage(files[i]);
			}

			survey.creator.createdSurveys.incrementBySurveyType(survey.type);
			this.userRep.save(survey.creator);
		} catch (Exception e) {
			throw new ConflictException();
		}

		return survey;
	}

	private boolean checkValid() {
		if (this.title == null || this.title.length() < 3 || this.title.length() > 15) {
			return false;
		}
		if (this.type == null) {
			return false;
		}
		if (this.rateCategories == null || this.rateCategories.size() > 3 || this.rateCategories.size() < 1) {
			return false;
		}
		if (this.priority == null) {
			return false; // Todo: Check when Sponsored || Paid the
							// pay-Authority So that everybody has paid
							// correctly
		}
		if (this.participants == null || this.participants.size() < 1 || this.participants.size() > 20
				|| this.participants.size() == 1 && this.participants.get(0) == this.tmpAuthID) {
			return false;
		}
		if (this.tmpFiles == null || this.tmpFiles.length != this.images.size()) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "SurveyPostHelper [title=" + this.title + ", type=" + this.type + ", rateCategories="
				+ this.rateCategories + ", priority=" + this.priority + ", members=" + this.participants + "]";
	}
}
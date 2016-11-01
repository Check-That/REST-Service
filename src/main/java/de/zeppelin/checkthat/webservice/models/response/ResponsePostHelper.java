package de.zeppelin.checkthat.webservice.models.response;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import de.zeppelin.checkthat.webservice.Application;
import de.zeppelin.checkthat.webservice.exceptions.BadRequestException;
import de.zeppelin.checkthat.webservice.exceptions.ConflictException;
import de.zeppelin.checkthat.webservice.exceptions.ForbiddenException;
import de.zeppelin.checkthat.webservice.persisetence.ResponseRepository;
import de.zeppelin.checkthat.webservice.persisetence.SurveyRepository;
import de.zeppelin.checkthat.webservice.persisetence.UserRepository;

@Configurable(autowire = Autowire.BY_TYPE)
@Service
public class ResponsePostHelper {
	public List<Integer> responseValues;

	private UserRepository userRep = Application.getContext().getBean(UserRepository.class);
	private SurveyRepository surveyRep = Application.getContext().getBean(SurveyRepository.class);
	private ResponseRepository answerRep = Application.getContext().getBean(ResponseRepository.class);

	private Response response = new Response();

	public ResponsePostHelper() {
	}

	public Response generateAnswer(Long surveyId, Long authId) {
		try {
			this.response.survey = this.surveyRep.findOne(surveyId);
			this.response.responseValues = this.responseValues; 
			this.response.responder = this.userRep.findOneByAuthId(authId);
		} catch (Exception e) {
			throw new ConflictException();
		}

		if (!checkValid()) {
			throw new BadRequestException();
		}

		try {
			this.response.survey.responses.add(this.response);
			this.surveyRep.save(this.response.survey);

			this.response = this.answerRep.findOneBySurveyIdAndResponderAuthId(surveyId, authId);
		} catch (Exception e) {
			throw new ConflictException();
		}

		return this.response;
	}

	private boolean checkValid() {
		if (this.responseValues == null || this.responseValues.size() != this.response.survey.rateCategories.size()) {
			return false;
		}
		if (!this.response.survey.participants.contains(this.response.responder)) {
			throw new ForbiddenException();
		}

		return true;
	}
}

package de.zeppelin.checkthat.webservice.models.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonView;

import de.zeppelin.checkthat.webservice.models.JSONViews;
import de.zeppelin.checkthat.webservice.models.survey.SurveyType;

@Embeddable
public class SurveyStatistic {
	@Column(name = "compare")
	@JsonView(JSONViews.nonSensitive.class)
	public int compare = 0;
	@Column(name = "rate")
	@JsonView(JSONViews.nonSensitive.class)
	public int rate = 0;

	public SurveyStatistic() {
	}

	public void incrementBySurveyType(SurveyType type) {
		switch (type) {
		case COMPARE:
			this.compare++;
			break;
		case RATE:
			this.rate++;
			break;
		}
	}

	@Override
	public String toString() {
		return "SurveyStatistic [choose=" + this.compare + ", rate=" + this.rate + "]";
	}
}

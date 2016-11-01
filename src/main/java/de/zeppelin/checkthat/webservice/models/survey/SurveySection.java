package de.zeppelin.checkthat.webservice.models.survey;

public class SurveySection {
	public String title;
	public Iterable<Survey> surveys;
	
	public SurveySection(String title, Iterable<Survey> surveys) {
		this.title = title;
		this.surveys = surveys;
	}
}

package de.zeppelin.checkthat.webservice.Models.vote;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "survey")
public class Survey {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	public String image;
	public String title;
	@Enumerated(EnumType.STRING)
	public SurveyType type;
	
	public Survey(){}
	
	public Survey(String title, String image) {
		this.title = title;
		this.image = image;
	}
}
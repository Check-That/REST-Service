package de.zeppelin.checkthat.webservice.Models.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import de.zeppelin.checkthat.webservice.Models.survey.Survey;

@Entity(name = "user")
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	@NotNull
	public String name;
	@Embedded
	public Privacy privacy = new Privacy();
	@Column(name = "image_path")
	public String imagePath = "";
	@ManyToMany
	public List<Survey> createdSurveys;
	
	protected User() {
	}

	public User(String name) {
		this.name = name;
	}
}

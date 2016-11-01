package de.zeppelin.checkthat.webservice.models.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonView;

import de.zeppelin.checkthat.webservice.models.JSONViews;

@Embeddable
public class Privacy {
	@Enumerated(EnumType.STRING)
	@Column(name = "privacy_image")
	@JsonView(JSONViews.nonSensitive.class)
	public Visibility image;
	@Enumerated(EnumType.STRING)
	@Column(name = "privacy_status")
	@JsonView(JSONViews.nonSensitive.class)
	public Visibility status;
	@Enumerated(EnumType.STRING)
	@Column(name = "privacy_votes")
	@JsonView(JSONViews.nonSensitive.class)
	public Visibility responses;

	public Privacy() {
		this.status = Visibility.All;
		this.responses = Visibility.All;
		this.image = Visibility.All;
	}

	@Override
	public String toString() {
		return "Privacy [image=" + this.image + ", status=" + this.status + ", responses=" + this.responses + "]";
	}
}

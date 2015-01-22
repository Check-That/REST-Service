package de.zeppelin.checkthat.webservice.Models.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class Privacy {
	@Enumerated(EnumType.STRING)
	@Column(name = "privacy_image")
	public Visibility image;
	@Enumerated(EnumType.STRING)
	@Column(name = "privacy_status")
	public Visibility status;
	@Enumerated(EnumType.STRING)
	@Column(name = "privacy_votes")
	public Visibility votes;

	public Privacy() {
		this.status = Visibility.all;
		this.votes = Visibility.all;
		this.image = Visibility.all;
	}
}

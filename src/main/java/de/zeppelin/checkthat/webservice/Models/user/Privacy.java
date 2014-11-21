package de.zeppelin.checkthat.webservice.Models.user;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class Privacy {
	@Enumerated(EnumType.STRING)
	public Visibility status;
	@Enumerated(EnumType.STRING)
	public Visibility votes;
	@Enumerated(EnumType.STRING)
	public Visibility image;

	public Privacy() {
		this.status = Visibility.all;
		this.votes = Visibility.all;
		this.image = Visibility.all;
	}	
}

package de.zeppelin.checkthat.webservice.models.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties("")
public class ProfileUser extends User {

	public ProfileUser() {
	}

	public ProfileUser(User u) {
		super(u.id, u.uniqueString, u.name, u.status, u.image, u.lastActivity, u.credits, u.createdSurveys, u.respondedSurveys,
				u.language, u.email, u.phone, u.privacy, u.authId, u.groups, u.friends, u.pushToken);
	}
	

	public ProfileUser(String name, String uniqueString) {
		super(name, uniqueString);
	}

	public User toUser() {
		return new User(this.id, this.uniqueString, this.name, this.status, this.image, this.lastActivity, this.credits,
				this.createdSurveys, this.respondedSurveys, this.language, this.email, this.phone, this.privacy,
				this.authId, this.groups, this.friends, this.pushToken);
	}

}

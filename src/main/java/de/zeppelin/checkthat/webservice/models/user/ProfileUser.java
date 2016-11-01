package de.zeppelin.checkthat.webservice.models.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties("")
public class ProfileUser extends User {

	
	public ProfileUser() {}
	
	
	public ProfileUser(User u) {
		super(u.id, u.uniqueString, u.name, u.status, u.lastActivity, u.credits, u.createdSurveys, u.respondedSurveys, u.language, u.email, u.phone,
				u.image, u.privacy, u.authId, u.circles, u.friends, u.pushToken);
	}


	public ProfileUser(String name) {
		super(name);
	}
	
	public User toUser()
	{
		return new User(this.id, this.uniqueString, this.name, this.status, this.lastActivity, this.credits, this.createdSurveys, this.respondedSurveys, this.language, this.email, this.phone,
				this.image, this.privacy, this.authId, this.circles, this.friends, this.pushToken);
	}
	
	
}

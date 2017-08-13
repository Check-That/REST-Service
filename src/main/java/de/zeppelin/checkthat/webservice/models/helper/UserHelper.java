package de.zeppelin.checkthat.webservice.models.helper;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.zeppelin.checkthat.webservice.exceptions.ForbiddenException;
import de.zeppelin.checkthat.webservice.exceptions.UnauthorizedException;
import de.zeppelin.checkthat.webservice.models.user.User;
import de.zeppelin.checkthat.webservice.persisetence.UserRepository;

@Component
public class UserHelper {
	
	@Autowired
	private UserRepository userRep;

	public User checkAuthId(Long authId) {
		
		if (authId != null && authId != 0) {
			User user = userRep.findOneByAuthId(authId);
			if (user != null) {
				user.lastActivity = new Date();
				return userRep.save(user);
			} else {
				throw new ForbiddenException();
			}
		} else {
			throw new UnauthorizedException();
		}
	}
}

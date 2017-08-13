package de.zeppelin.checkthat.webservice.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;

import de.zeppelin.checkthat.webservice.exceptions.BadRequestException;
import de.zeppelin.checkthat.webservice.exceptions.ConflictException;
import de.zeppelin.checkthat.webservice.exceptions.UsernameAlreadyExistsException;
import de.zeppelin.checkthat.webservice.models.JSONViews;
import de.zeppelin.checkthat.webservice.models.helper.ProfileUserPostHelper;
import de.zeppelin.checkthat.webservice.models.helper.UniqueStringGenerator;
import de.zeppelin.checkthat.webservice.models.helper.UserHelper;
import de.zeppelin.checkthat.webservice.models.user.ProfileUser;
import de.zeppelin.checkthat.webservice.models.user.User;
import de.zeppelin.checkthat.webservice.persisetence.SurveyRepository;
import de.zeppelin.checkthat.webservice.persisetence.UserRepository;

@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	UserRepository userRep;

	@Autowired
	SurveyRepository surveyRep;
	
	@Autowired
	UserHelper userHelper;

	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Iterable<User> getAll() {
		return this.userRep.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	@JsonView(JSONViews.sensitive.class)
	public User newUser(@RequestBody ProfileUser user) {
		if (user == null) {
			throw new BadRequestException();
		}
		// Neu registrieren
		List<String> isoLanguages = Arrays.asList(Locale.getISOLanguages());
		if (user.name == null || user.name.length() < 3 || user.uniqueString == null || user.uniqueString.length() < 3
				|| !isoLanguages.contains(user.language)) {
			throw new BadRequestException();
		}
		if (this.userRep.existsByUniqueString(user.uniqueString)) {
			throw new UsernameAlreadyExistsException();
		}
		try {
			user.email = user.email.toLowerCase();
			do {
				user.authId = UUID.randomUUID().getMostSignificantBits();
			} while (user.authId >= 0);
			User newUser = this.userRep.save(user.toUser());
			return new ProfileUser(newUser);
		} catch (Exception ex) {
			throw new ConflictException();
		}
	}

	@RequestMapping(path = "checkUID", method = RequestMethod.GET)
	public String checkUID(@RequestParam(name = "uid ") String uid) {
		if (!this.userRep.existsByUniqueString(uid)) {
			return "ok";
		} else {
			return "false";
		}
	}

	@RequestMapping(path = "checkPhone", method = RequestMethod.GET)
	public String checkPhoneNumber(@RequestParam(name = "phone") String phone) {
		if (!this.userRep.existsByPhoneNumber(phone.replace(" ", ""))) {
			return "ok";
		} else {
			return "false";
		}
	}

	@RequestMapping(path = "checkEMail", method = RequestMethod.GET)
	public String checkEMail(@RequestParam(name = "email") String email) {
		if (!this.userRep.existsByEmail(email.toLowerCase())) {
			return "ok";
		} else {
			return "false";
		}
	}

	@RequestMapping(path = "uniqueStringSuggestions", method = RequestMethod.GET)
	public List<String> getUniqueStringSuggestions(@RequestParam(name = "name", required = true) String name) {
		return UniqueStringGenerator.getSuggestions(name);
	}

	@RequestMapping("{uniqueString}")
	@JsonView(JSONViews.nonSensitive.class)
	public User getUserById(@PathVariable("uniqueString") String uniqueString) {
		return this.userRep.findOneByUniqueString(uniqueString);
	}

	@RequestMapping(path = "/profile", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ProfileUser getProfile(@RequestHeader(name = "authId") Long authId) {
		User user = userHelper.checkAuthId(authId);
		ProfileUser prof = new ProfileUser(user);
		return prof;
	}

	@RequestMapping(path = "/profile", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	@Transactional
	public ProfileUser setProfile(@RequestHeader(name = "authId") Long authId,
			@RequestBody ProfileUserPostHelper helper) {
		userHelper.checkAuthId(authId);
		ProfileUser user = helper.updateUser(authId);
		
		return user;
	}

	@RequestMapping(path = "/image", method = RequestMethod.POST)
	public String setImage(@RequestHeader(value = "authId", required = false) Long authId, 
			@RequestParam(name = "image", required = true) MultipartFile file) {

		if (file == null || file.getSize() == 0) { throw new BadRequestException();	}
		
		User user = userHelper.checkAuthId(authId);

		user.saveImage(file);
		userRep.save(user); // Save the (new) AWS-Object ID

		return "ok";
	}
	
	@RequestMapping(path = "/image", method = RequestMethod.DELETE)
	public String removeImage(@RequestHeader(value = "authId", required = false) Long authId) {
		
		User user = userHelper.checkAuthId(authId);

		user.image = "";
		userRep.save(user);

		return "ok";
	}
}
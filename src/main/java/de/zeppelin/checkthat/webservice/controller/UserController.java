package de.zeppelin.checkthat.webservice.controller;

import java.util.List;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import de.zeppelin.checkthat.webservice.exceptions.BadRequestException;
import de.zeppelin.checkthat.webservice.exceptions.ConflictException;
import de.zeppelin.checkthat.webservice.exceptions.ForbiddenException;
import de.zeppelin.checkthat.webservice.exceptions.UnauthorizedException;
import de.zeppelin.checkthat.webservice.exceptions.UsernameAlreadyExistsException;
import de.zeppelin.checkthat.webservice.models.JSONViews;
import de.zeppelin.checkthat.webservice.models.survey.Survey;
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

	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Iterable<User> getAll() {
		return this.userRep.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	@JsonView(JSONViews.sensitive.class)
	public User newUser(@RequestBody ProfileUser user) {
		if (user == null) throw new BadRequestException();
		//Bereits registriert, nur anmelden
		if (user.authId != null && user.authId != 0) {
			User regUser = this.userRep.findOneByAuthId(user.authId);
			if (regUser == null) throw new ForbiddenException();
			return new ProfileUser(regUser);
		} 

		//Neu registrieren 
		Locale[] isocodes = Locale.getAvailableLocales();
		System.out.println(isocodes);
		if (user.name == null || user.name.length() < 3 || user.uniqueString == null || user.uniqueString.length() < 3) throw new BadRequestException();
		if (this.userRep.existsByUniqueString(user.uniqueString)) throw new UsernameAlreadyExistsException();
		try {
			user.authId = UUID.randomUUID().getMostSignificantBits();
			User newUser = this.userRep.save(user.toUser());
			return new ProfileUser(newUser);
		} catch(Exception ex) { throw new ConflictException();}	
	}

	@RequestMapping(path = "checkUniqueString", method = RequestMethod.GET, params = {"uniqueString"})
	public String checkUniqueString(@RequestParam("uniqueString")String uniqueString) {
		if (!this.userRep.existsByUniqueString(uniqueString)) {
			return "ok";
		} else {
			return "false";
		}
	}

	@RequestMapping(path = "uniqueStringSuggestions", method = RequestMethod.GET, params = {"name"})
	public List<String> getUniqueStringSuggestions(@RequestParam("name")String name) {
		ArrayList<String> suggestions = new ArrayList<String>();
		String baseName = escapeString(name);

		if (baseName.split(" ").length <= 2)) {
			suggestions.add(checkNameVariationGroup(baseName));
			String[] split = baseName.split(" ");
			String complete = String.join("", split);
			suggestions.add(checkNameVariationGroup(split[0].substring(0, 1) + " " + complete.substring(split[0].length(), complete.length())));
			suggestions.add(checkNameVariationGroup(complete.substring(0, complete.length() - split[split.length - 1].length()) + split[split.length - 1].substring(0,1)));
		} else {
			
		}
		
		//Zahlen anhängen
		baseName = baseName.replace(" ", "");
		while (suggestions.size() < 3)
		{
			name = baseName + randInt(1, 1000);
			System.out.println(name);
			if (name.length() >= 3 && !this.userRep.existsByUniqueString(name)) {
				System.out.println("check");
				suggestions.add(name);
			}
		}

		return suggestions;
	}

	private String checkNameVariationGroup(String name) {
		String suboptimalName = null;
		for (ReplacementLetter letter : ReplacementLetter.values()) {
			String tmpName = getAvailableName(name, letter);
			if (tmpName != null) return tmpName;
			for (int i = 0; suboptimalName == null && i < 3; i++) {
				tmpName = getAvailableName(name + randInt(0, 1000), letter);
				if (tmpName != null) {
					suboptimalName = tmpName;
					break;
				}
			}
		}
		return suboptimalName;
	}

	private String getAvailableName(String name, ReplacementLetter replacementLetter) {
		name = name.replace(" ", replacementLetter.toString());
		System.out.println(name);
		if (name.length() >= 3 && !this.userRep.existsByUniqueString(name)) {
			System.out.println("check");
			return name;
		}
		return null;
	}



	private static int randInt(int min, int max) {

		// Usually this can be a field rather than a method variable
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	private String escapeString(String name) {
		String accepted = "abcdefghijklmnopqrstuvwxyz123456789._-+*/ ";

		name = name.trim().toLowerCase();
		char[] out = new char[name.length()];
		name = Normalizer.normalize(name, Normalizer.Form.NFD);
		int j = 0;
		for (int i = 0, n = name.length(); i < n; ++i) {
			char c = name.charAt(i);
			if (accepted.contains(""+c)) out[j++] = c;
		}
		
		// if name too short -> predefined name
		if (name.length() < 3) return "Poll Master";
		
		return new String(out);
	}

	@RequestMapping("{id}")
	@JsonView(JSONViews.sensitive.class)
	public ProfileUser getUserById(@PathVariable("id") String id) {
		User user = this.userRep.findOne(Long.parseLong(id));
		return new ProfileUser(user);
	}

	@RequestMapping("{id}/created")
	@JsonView(JSONViews.nonSensitive.class) 
	public Iterable<Survey> getCreatedSurveysByUser(@PathVariable("id") Long id) {
		return this.surveyRep.findByCreator(this.userRep.findOne(id));
	}

	@RequestMapping("{id}/participating")
	@JsonView(JSONViews.nonSensitive.class) 
	public Iterable<Survey> getParticipatingSurveysByUser(@PathVariable("id") Long id) {
		return this.surveyRep.findByParticipants(this.userRep.findOne(id));
	}

	@SuppressWarnings("unused")
	private void checkAuthId(Long authId)
	{
		if (authId != null && authId != 0) {
			User user = this.userRep.findOneByAuthId(authId);
			if (user != null) {
				user.lastActivity = new Date();
				userRep.save(user);
			} else {
				throw new ForbiddenException();
			}
		} else {
			throw new UnauthorizedException();
		}
	}
}
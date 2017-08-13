package de.zeppelin.checkthat.webservice.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.zeppelin.checkthat.webservice.exceptions.ConflictException;
import de.zeppelin.checkthat.webservice.models.user.User;
import de.zeppelin.checkthat.webservice.persisetence.SurveyRepository;
import de.zeppelin.checkthat.webservice.persisetence.UserRepository;

@Controller
@RequestMapping("init")
public class InitController {

	@Autowired
	UserRepository userRep;
	@Autowired
	SurveyRepository surveyRep;

	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public String initGeneral() {
		User cedric = new User("Cedric", "TheCED");
		User yannick = new User("Yannick", "YMaster");
		User horst = new User("Horst", "Horscht");

		 //this.userRep.save(cedric);
		 //this.userRep.save(yannick);
		 //this.userRep.save(horst);

		cedric = this.userRep.findOne(new Long("1"));
		yannick = this.userRep.findOne(new Long("2"));
		horst = this.userRep.findOne(new Long("3"));

//		Group circle = new Group();
//		circle.name = "TestGroup";
//		circle.owner = cedric;
//		circle.members.add(yannick);
//		circle.members.add(horst);
//
//		cedric.groups.add(circle);
		
		cedric.friends.add(yannick);
		cedric.friends.add(horst);
		horst.friends.add(cedric);

		this.userRep.save(cedric);
		this.userRep.save(horst);
		//
		// List<User> members = new ArrayList<User>();
		// members.add(horst);
		// members.add(yannick);
		//
		// List<String> categories = new ArrayList<String>();
		// categories.add("face");
		// categories.add("beauty");
		//
		// ArrayList<Image> imageList = new ArrayList<Image>();
		// imageList.add(new Image());
		// imageList.add(new Image());
		// imageList.add(new Image());
		//
		// Survey shoes = new Survey(cedric, imageList, "shoes", categories,
		// SurveyType.TopFlop, members);
		//
		// Survey watch = new Survey(cedric, imageList, "watch", categories,
		// SurveyType.COMPARE, members);
		//
		// Survey watch2 = new Survey(cedric, imageList, "watches", categories,
		// SurveyType.RATE, members);
		//
		// this.surveyRep.save(watch2);
		// this.surveyRep.save(watch);
		// this.surveyRep.save(shoes);

		return "ok";
	}
	
	@RequestMapping(path = "friends", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public String frindFriends() {
		User user = userRep.findOne(1l);
		try {
			ArrayList<Long> ids = new ArrayList<Long>();
			ids.add(2l);
			ids.add(3l);
			ArrayList<User> friends = new ArrayList<User>();
			for (Long id : ids) {
				User friend = userRep.findOne(id);
				if (friend == null) {
					throw new Exception();
				}
				friends.add(friend);
			}
			user.friends.addAll(friends);
			userRep.save(user);
		} catch (Exception e) {
			throw new ConflictException();
		}
		
		return "ok";
	}
}

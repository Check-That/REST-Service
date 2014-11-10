package de.zeppelin.checkthat.webservice.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.util.JSONPObject;

import de.zeppelin.checkthat.webservice.Models.Privacy;
import de.zeppelin.checkthat.webservice.Models.User;
import de.zeppelin.checkthat.webservice.persicetence.UserRepository;

@Controller
@RequestMapping("user")
public class RestController {

	@Autowired
	UserRepository repository;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Iterable<User> sayHello() {
		repository.save(new User("Yannick"));
		repository.save(new User("Cedric"));
		return repository.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public JSONPObject newUser(@RequestBody User user) {
		if (user != null && user.name != null && !user.name.isEmpty()) {
			user.privacy = new Privacy();
			User newUser = repository.save(user);
			return new JSONPObject("id", newUser.id);
		} else {
			return new JSONPObject("error", "missing user details");
		}

	}
}
package de.zeppelin.checkthat.webservice.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.zeppelin.checkthat.webservice.Models.user.Privacy;
import de.zeppelin.checkthat.webservice.Models.user.User;
import de.zeppelin.checkthat.webservice.persicetence.UserRepository;

@Controller
@RequestMapping("user")
public class RestController {

	@Autowired
	UserRepository repository;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Iterable<User> sayHello() {
		return repository.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public String newUser(@RequestBody User user) {
		if (user != null && user.name != null && !user.name.isEmpty()) {
			user.privacy = new Privacy();
			User newUser = repository.save(user);
			return "{\"id\":"+newUser.id+"}";
		} else {
			return "error";
		}

	}
	
	@RequestMapping("init")
	@ResponseBody
	public String initUsers() {
		repository.save(new User("Yannick"));
		repository.save(new User("Cedric"));
		
		return "ok";
	}
	
	@RequestMapping("{id}")
	@ResponseBody
	public User getUserById(@PathVariable("id")String id) {
		return repository.findOne(Long.parseLong(id));
	}
}
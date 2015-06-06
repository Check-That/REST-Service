package de.zeppelin.checkthat.webservice.Models.user.circle;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

import de.zeppelin.checkthat.webservice.Models.user.User;

@Entity
public class Circle {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	@JoinTable(name = "circleMembers")
	public List<User> members;
	public String name;
	@ManyToOne
	public User owner;
}

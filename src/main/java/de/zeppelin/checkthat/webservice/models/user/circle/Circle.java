package de.zeppelin.checkthat.webservice.models.user.circle;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import de.zeppelin.checkthat.webservice.models.user.User;

@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Circle {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	@JoinTable(name = "circleMembers")
	public List<User> members = new ArrayList<User>();
	public String name;
	@ManyToOne
	public User owner;
}

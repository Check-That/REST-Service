package de.zeppelin.checkthat.webservice.models.user.group;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import de.zeppelin.checkthat.webservice.models.user.User;

@Entity
@Table(name = "usergroups")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Group implements Persistable<Long> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	public String name;
	@ManyToOne
	public User owner;
	@JoinTable(name = "groupMembers")
	public List<User> members = new ArrayList<User>();

	public Group() {
	}

	public Group(String name) {
		super();
		this.name = name;
	}

	@Override
	public String toString() {
		return "Group [id=" + this.id + ", name=" + this.name + ", owner=" + this.owner + ", members=" + this.members
				+ "]";
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public boolean isNew() {
		return null == getId();
	}
}

package de.zeppelin.checkthat.webservice.models.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import de.zeppelin.checkthat.webservice.Application;
import de.zeppelin.checkthat.webservice.exceptions.BadRequestException;
import de.zeppelin.checkthat.webservice.exceptions.ConflictException;
import de.zeppelin.checkthat.webservice.models.user.Privacy;
import de.zeppelin.checkthat.webservice.models.user.ProfileUser;
import de.zeppelin.checkthat.webservice.models.user.User;
import de.zeppelin.checkthat.webservice.models.user.group.Group;
import de.zeppelin.checkthat.webservice.persisetence.GroupRepository;
import de.zeppelin.checkthat.webservice.persisetence.UserRepository;

public class ProfileUserPostHelper {

	public String name = "";
	// public String image; // TODO: Save image 
	public String status = "";
	public Privacy privacy;
	public String language;
	public String email;
	public String phone;
	public List<GroupPostHelper> groups;
	public List<Long> friends;

	private UserRepository userRep = Application.getContext().getBean(UserRepository.class);
	private GroupRepository groupRep = Application.getContext().getBean(GroupRepository.class);
	private Long authId;

	public ProfileUser updateUser(Long authId) {
		this.authId = authId;

		if (!checkValid()) {
			throw new BadRequestException();
		}

		User user = this.userRep.findOneByAuthId(authId);
		user = setAttributes(user);

		user = this.userRep.save(user);

		return new ProfileUser(user);
	}

	private boolean checkValid() {
		List<String> isoLanguages = Arrays.asList(Locale.getISOLanguages());

		if (this.name == null || this.name.length() < 3 || this.name.length() > 255) {
			return false;
		}
		if (this.status == null || this.status.length() > 255) {
			return false;
		}
		if (this.privacy == null || this.privacy.image == null || this.privacy.status == null
				|| this.privacy.votes == null) {
			return false;
		}
		if (this.language == null || !isoLanguages.contains(this.language)) {
			return false;
		}
		if (this.groups == null) {
			return false;
		}
		if (this.friends == null) {
			return false;
		}

		return true;
	}

	private User setAttributes(User user) {
		user = userRep.findOneByAuthId(user.authId);
		user.name = this.name;
		user.status = this.status;
		user.privacy = this.privacy;
		user.language = this.language;
		user.email = this.email;
		user.phone = this.phone;

		// Friends
		

		try {
			ArrayList<User> friends = new ArrayList<User>();
			for (Long id : this.friends) {
				User friend = userRep.findOne(id);
				if (friend == null) {
					throw new Exception();
				}
				friends.add(friend);
			}
			user.friends = friends;
		} catch (Exception e) {
			throw new ConflictException();
		}

		// Groups
		List<Group> newGroups = new ArrayList<Group>();
		for (GroupPostHelper groupPostHelper : this.groups) {
			newGroups.add(groupPostHelper.generateGroup(this.authId));
		}

		newGroups = (List<Group>) this.groupRep.save(newGroups);
		List<Long> newGroupIds = new ArrayList<Long>();
		user.groups.size(); // To enforce the indirect list to load
		for (Group group : newGroups) {
			newGroupIds.add(group.id);
		}
		for (int i = 0; i < user.groups.size();) {
			if (newGroupIds.contains(user.groups.get(i).id)) {
				i++;
			} else {
				user.groups.remove(user.groups.get(i));
				// user.groups.get(i).owner = null;
			}
		}

		return user;
	}

	public static List<User> usersForIds(List<Long> ids) {
		UserRepository userRep = Application.getContext().getBean(UserRepository.class);
		ArrayList<User> users = new ArrayList<User>();
		try {
			for (Long id : ids) {
				User friend = userRep.findOne(id);
				if (friend == null) {
					throw new Exception();
				}
				users.add(friend);
			}
		} catch (Exception e) {
			throw new ConflictException();
		}

		return users;
	}
}

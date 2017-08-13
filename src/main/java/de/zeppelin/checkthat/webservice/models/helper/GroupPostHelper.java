package de.zeppelin.checkthat.webservice.models.helper;

import java.util.List;

import de.zeppelin.checkthat.webservice.Application;
import de.zeppelin.checkthat.webservice.exceptions.BadRequestException;
import de.zeppelin.checkthat.webservice.exceptions.ConflictException;
import de.zeppelin.checkthat.webservice.exceptions.ForbiddenException;
import de.zeppelin.checkthat.webservice.models.user.User;
import de.zeppelin.checkthat.webservice.models.user.group.Group;
import de.zeppelin.checkthat.webservice.persisetence.GroupRepository;
import de.zeppelin.checkthat.webservice.persisetence.UserRepository;

public class GroupPostHelper {
	public Long id;
	public String name;
	public List<Long> members;

	private GroupRepository groupRep = Application.getContext().getBean(GroupRepository.class);
	private UserRepository userRep = Application.getContext().getBean(UserRepository.class);

	public GroupPostHelper() {
	}

	public Group generateGroup(Long authId) {
		if (!checkValid()) {
			throw new BadRequestException();
		}

		Group group;

		if (this.id == null) {
			// new Group
			group = new Group(this.name);
			group.members = ProfileUserPostHelper.usersForIds(this.members);
			User owner = this.userRep.findOneByAuthId(authId);
			if (owner == null) {
				throw new ConflictException();
			}
			group.owner = owner;

		} else {
			// Existing Group
			group = this.groupRep.findOne(this.id);
			if (group == null) {
				throw new ConflictException();
			}
			if (!group.owner.authId.equals(authId)) {
				throw new ForbiddenException();
			}

			group.name = this.name;
			group.members = ProfileUserPostHelper.usersForIds(this.members);
		}

		return group;
	}

	private boolean checkValid() {
		if (this.name == null || this.name.length() < 1 || this.name.length() > 255) {
			return false;
		}
		if (this.members == null || this.members.isEmpty()) {
			return false;
		}
		return true;
	}

}

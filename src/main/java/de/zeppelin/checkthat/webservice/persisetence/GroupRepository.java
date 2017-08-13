package de.zeppelin.checkthat.webservice.persisetence;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.zeppelin.checkthat.webservice.models.user.group.Group;

@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {

	@Modifying
	@Query("DELETE FROM Group g WHERE g.id NOT IN :groupIds AND g.owner.authId = :authId")
	void deleteOtherGroups(@Param("groupIds") List<Long> groups, @Param("authId") Long authId);
}

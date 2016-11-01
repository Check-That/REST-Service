package de.zeppelin.checkthat.webservice.persisetence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.zeppelin.checkthat.webservice.models.image.Image;

@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {
	@Query("SELECT i FROM image i WHERE i.survey.id = :surveyId AND i.uploaded = FALSE")
	Iterable<Image> findEmptyImageBySurveyId(@Param("surveyId") Long surveyId);

	@Query("SELECT DISTINCT(i) FROM image i, user u WHERE i.id = :imageId AND u.authId = :authId AND (i.survey.creator = u OR u MEMBER OF i.survey.participants)")
	Image getImageByImageIdAndAuthId(@Param("imageId") Long imageId, @Param("authId") Long authId);
}

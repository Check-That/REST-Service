package de.zeppelin.checkthat.webservice.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amazonaws.services.s3.model.S3Object;

import de.zeppelin.checkthat.webservice.exceptions.ConflictException;
import de.zeppelin.checkthat.webservice.exceptions.ForbiddenException;
import de.zeppelin.checkthat.webservice.exceptions.ImageNotFoundException;
import de.zeppelin.checkthat.webservice.exceptions.InternalServerErrorException;
import de.zeppelin.checkthat.webservice.models.helper.UserHelper;
import de.zeppelin.checkthat.webservice.models.image.Image;
import de.zeppelin.checkthat.webservice.models.image.ProfileImageType;
import de.zeppelin.checkthat.webservice.models.image.SurveyImageType;
import de.zeppelin.checkthat.webservice.models.user.User;
import de.zeppelin.checkthat.webservice.persisetence.ImageRepository;
import de.zeppelin.checkthat.webservice.persisetence.SurveyRepository;
import de.zeppelin.checkthat.webservice.persisetence.UserRepository;

@Controller
@RequestMapping("images")
public class ImageController {

	@Autowired
	SurveyRepository surveyRep;
	@Autowired
	UserRepository userRep;
	@Autowired
	ImageRepository imageRepository;

	@Autowired
	UserHelper userHelper;

	// ImageId und Type sind durch ein @ getrennt -> "000123456751@small" oder
	// "000123456751" für Originalgröße
	@RequestMapping(value = "/survey/{imageId}", method = RequestMethod.GET, produces = { MediaType.IMAGE_JPEG_VALUE })
	@ResponseBody
	public InputStreamResource getSurveyImage(@PathVariable("imageId") String imageId, @RequestParam("type") SurveyImageType imageType,
			@RequestHeader(value = "authId", required = false) Long authId, HttpServletResponse response) {

		userHelper.checkAuthId(authId);

		Image image = this.imageRepository.getImageByImageIdAndAuthId(Long.parseLong(imageId), authId);
		if (image == null) {
			throw new ForbiddenException();
		}
		if (!image.uploaded) {
			throw new ConflictException();
		}

		try {

			S3Object imageObject = image.getImage(imageType);

			response.addHeader("id", imageId);
			response.addHeader("fullName", imageObject.getKey());
			response.setContentLength((int) imageObject.getObjectMetadata().getContentLength());

			return new InputStreamResource(imageObject.getObjectContent());
		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerErrorException();
		}
	}

	@RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET, produces = { MediaType.IMAGE_JPEG_VALUE })
	@ResponseBody
	public InputStreamResource getProfileImage(@PathVariable("userId") Long userId,@RequestParam("type") ProfileImageType imageType,
			@RequestHeader(value = "authId", required = false) Long authId, HttpServletResponse response) {
		userHelper.checkAuthId(authId);

		User user = userRep.findOne(userId);
		if (user == null) throw new ConflictException();
		if (user.image.equals("")) throw new ImageNotFoundException();
		
		try {
			S3Object imageObject = user.getImage(imageType);

			response.addHeader("id", user.image);
			response.addHeader("fullName", imageObject.getKey());
			response.setContentLength((int) imageObject.getObjectMetadata().getContentLength());

			return new InputStreamResource(imageObject.getObjectContent());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ImageNotFoundException();
		}
	}
}
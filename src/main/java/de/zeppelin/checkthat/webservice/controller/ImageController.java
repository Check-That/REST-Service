package de.zeppelin.checkthat.webservice.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import de.zeppelin.checkthat.webservice.exceptions.BadRequestException;
import de.zeppelin.checkthat.webservice.exceptions.ConflictException;
import de.zeppelin.checkthat.webservice.exceptions.ForbiddenException;
import de.zeppelin.checkthat.webservice.exceptions.InternalServerErrorException;
import de.zeppelin.checkthat.webservice.exceptions.UnauthorizedException;
import de.zeppelin.checkthat.webservice.models.image.Image;
import de.zeppelin.checkthat.webservice.models.survey.Survey;
import de.zeppelin.checkthat.webservice.persisetence.ImageRepository;
import de.zeppelin.checkthat.webservice.persisetence.SurveyRepository;
import de.zeppelin.checkthat.webservice.persisetence.UserRepository;
import jersey.repackaged.com.google.common.collect.Lists;

@Controller
@RequestMapping("images")
public class ImageController {

	@Autowired
	SurveyRepository surveyRep;
	@Autowired
	UserRepository userRep;
	@Autowired
	ImageRepository imageRepository;

	// ImageId und Type sind durch ein @ getrennt -> "000123456751@small" oder "000123456751" für Originalgröße
	@RequestMapping(value = "{imageIdAndType}", method = RequestMethod.GET, produces = {MediaType.IMAGE_JPEG_VALUE})
	@ResponseBody
	public byte[] getImage(@PathVariable("imageIdAndType") String imageId, @RequestHeader(value = "authId", required = false) Long authId) {
		if (authId == null) throw new UnauthorizedException();
		String[] imageIdAndType = imageId.split("@");
		Image image = this.imageRepository.getImageByImageIdAndAuthId(Long.parseLong(imageIdAndType[0]), authId);
		if (image == null) throw new ForbiddenException();
		
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		
		try {
			String postfix = imageIdAndType.length > 1 ? imageIdAndType[1] : "";
			ImageIO.write(image.getImage(postfix), "jpg", bao);
		} catch (IOException e) {
			e.printStackTrace();
			throw new InternalServerErrorException();
		}
		
		return bao.toByteArray();
	}
	
	@RequestMapping(value = "{imageId}", method = RequestMethod.POST)
	@ResponseBody
	public String postImages(@PathVariable("imageId") Long imageId, @RequestHeader(value = "authId", required = false) Long authId, @RequestParam(name = "image", required = true) MultipartFile file) {
		if (file == null || file.getSize() == 0) throw new BadRequestException();
		if (authId == null || authId == 0 ) throw new UnauthorizedException();

		Image image = imageRepository.findOne(imageId);

		if (image == null) throw new ConflictException();
		if (image.survey.creator.authId.longValue() != authId.longValue()) throw new ForbiddenException();

		image.saveImage(file);
		
		return "ok";
	}
}
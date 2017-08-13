package de.zeppelin.checkthat.webservice.models.image;

import java.awt.image.BufferedImage;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.annotation.JsonIgnore;

import de.zeppelin.checkthat.webservice.Application;
import de.zeppelin.checkthat.webservice.exceptions.InternalServerErrorException;
import de.zeppelin.checkthat.webservice.models.helper.ImageHelper;
import de.zeppelin.checkthat.webservice.models.survey.Survey;
import de.zeppelin.checkthat.webservice.persisetence.ImageRepository;

@Entity(name = "image")
@Table(name = "images")
// @JsonIgnoreProperties(value = {"survey", "uploaded"})
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	public String title;
	@JsonIgnore
	public String awsObjectKey = UUID.randomUUID().toString();

	@JsonIgnore
	public boolean uploaded = false;

	@ManyToOne(optional = false)
	@JsonIgnore
	public Survey survey;

	public Image() {
	}

	@Async
	public void saveImage(MultipartFile mpFile) {

		// Generate thumbnail versions+
		try {
			BufferedImage bImage = ImageIO.read(mpFile.getInputStream());
			for (SurveyImageType imageType : SurveyImageType.values()) {
				BufferedImage scaledImage = ImageHelper.scaleImage(bImage, imageType.width, imageType.height);
				
				ImageHelper.saveToAWS(scaledImage, Application.surveyImagesBucketName, this.awsObjectKey + imageType.postfix + ".jpg");
			}
		} catch (Exception e) {
			throw new InternalServerErrorException();
		}

		ImageRepository imageRep = Application.getContext().getBean(ImageRepository.class);

		this.uploaded = true;

		imageRep.save(this);

	}

	public S3Object getImage(SurveyImageType imageType) {
		return ImageHelper.getImage(Application.surveyImagesBucketName, this.awsObjectKey + imageType.postfix + ".jpg");
	}

	// public String getImagePath() {
	// return getImagePath("");
	// }
	//
	//
	// public String getImagePath(String postfix) {
	// if (this.id == null || this.id == 0) {
	// return null;
	// }
	// String rootDir = "C:\\checkthat\\img\\survey";
	//
	// // Fill up id with zeros to get 12 digits
	// String fileName = String.format("%012d", this.id);
	//
	// if (postfix != null || postfix != "") {
	// postfix = "@" + postfix;
	// }
	//
	// Path path = Paths.get(rootDir, fileName.substring(0, 3),
	// fileName.substring(3, 6), fileName.substring(6, 9),
	// fileName + postfix + ".jpg");
	//
	// return path.toString();
	// }
}
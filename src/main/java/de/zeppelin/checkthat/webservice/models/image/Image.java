package de.zeppelin.checkthat.webservice.models.image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.zeppelin.checkthat.webservice.Application;
import de.zeppelin.checkthat.webservice.exceptions.InternalServerErrorException;
import de.zeppelin.checkthat.webservice.models.survey.Survey;
import de.zeppelin.checkthat.webservice.persisetence.ImageRepository;

@Entity(name = "image")
@Table(name = "image")
//@JsonIgnoreProperties(value = {"survey", "uploaded"})
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	public String title;

	@JsonIgnore
	public boolean uploaded = false;
	
	@ManyToOne(optional = false)
	@JsonIgnore
	public Survey survey;
	
	public Image() {}

	@JsonIgnore
	public String getImagePath() {return getImagePath("");}
	
	public String getImagePath(String postfix) {
		if (this.id == null || this.id == 0) return null;
		String rootDir = "C:\\checkthat\\pictures";

		// Fill up id with zeros to get 12 digits
		String fileName = String.format("%012d", id);
		
		Path path = Paths.get(rootDir, fileName.substring(0,3), fileName.substring(3,6), fileName.substring(6,9), fileName + postfix + ".jpg");
		
		return path.toString();
	}
	
	@Async
	public void saveImage(MultipartFile mpFile) {
		File targetFile = new File(getImagePath());
		try {
			// Save full-size Image
			targetFile.getParentFile().mkdirs();
			targetFile.createNewFile();
			mpFile.transferTo(targetFile);
			
			// Generate thumbnail versions
			BufferedImage bImage = ImageIO.read(mpFile.getInputStream());
			scaleImage(bImage, 600, 800, new File(getImagePath("@small")));
			scaleImage(bImage, 300, 400, new File(getImagePath("@2")));
			
			
			ImageRepository imageRep = Application.getContext().getBean(
					ImageRepository.class);
			
			this.uploaded = true;
			
			imageRep.save(this);
			
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			throw new InternalServerErrorException();
		}
	}
	
	public BufferedImage getImage(String postfix) {
		try {
			return ImageIO.read(new File(this.getImagePath(postfix)));
		} catch (IOException e) {
			e.printStackTrace();
			throw new InternalServerErrorException();
		}
	}
	
	public void scaleImage(BufferedImage image, int width, int height, File targetFile) throws IOException {
		BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		
		ImageIO.write(resizedImage, "jpg", targetFile);
	}
}
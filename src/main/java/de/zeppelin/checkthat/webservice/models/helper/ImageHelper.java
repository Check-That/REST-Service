package de.zeppelin.checkthat.webservice.models.helper;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import de.zeppelin.checkthat.webservice.Application;
import de.zeppelin.checkthat.webservice.exceptions.InternalServerErrorException;

public class ImageHelper {

	public static BufferedImage getImage(String path) {
		try {
			return ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
			throw new InternalServerErrorException();
		}
	}

	public static void scaleAndSaveImage(BufferedImage image, int width, int height, File targetFile)
			throws IOException {
		BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		ImageIO.write(resizedImage, "jpg", targetFile);
	}
	
	
	public static BufferedImage scaleImage(BufferedImage image, int width, int height) throws IOException {
		BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();

		return resizedImage;
	}
	
	public static S3Object getImage(String bucket, String awsObjectKey) {
		try {
			BasicAWSCredentials creds = new BasicAWSCredentials(Application.awsAccessKey, Application.awsSecretKey);
			AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
					.withCredentials(new AWSStaticCredentialsProvider(creds)).withRegion(Regions.US_WEST_2).build();

			S3Object object = s3Client.getObject(bucket, awsObjectKey);
			return object;
		} catch (Exception e) {
			throw new InternalServerErrorException();
		}
	}
	
	public static void saveToAWS(BufferedImage image, String awsBucketName, String awsObjectKey) throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", os);
		byte[] buffer = os.toByteArray();
		InputStream is = new ByteArrayInputStream(buffer);
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentLength(buffer.length);
		meta.setContentType("image/jpeg");

		BasicAWSCredentials creds = new BasicAWSCredentials(Application.awsAccessKey, Application.awsSecretKey);
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds))
				.withRegion(Regions.US_WEST_2).build();

		try {
			s3Client.putObject(awsBucketName, awsObjectKey, is, meta);

		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which " + "means your request made it "
					+ "to Amazon S3, but was rejected with an error response" + " for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
			throw ase;
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which " + "means the client encountered "
					+ "an internal error while trying to " + "communicate with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
			throw ace;
		}
	}

}

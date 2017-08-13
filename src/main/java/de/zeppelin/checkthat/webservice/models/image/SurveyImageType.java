package de.zeppelin.checkthat.webservice.models.image;

public enum SurveyImageType {
	original("original", "", 1500, 2000), thumbnail("thumbnail", "@tn", 360, 480), small("small", "@small", 600, 800);

	public final String description;
	public final String postfix;
	public final int width;
	public final int height;

	private SurveyImageType(String description, String postfix, int width, int height) {
		this.description = description;
		this.postfix = postfix;
		this.width = width;
		this.height = height;
	}
}

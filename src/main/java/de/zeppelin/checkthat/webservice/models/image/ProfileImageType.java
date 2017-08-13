package de.zeppelin.checkthat.webservice.models.image;

public enum ProfileImageType  {
	
	original("original", "", 1000, 1000), thumbnail("thumbnail", "@tn", 200, 200);

	public final String description;
	public final String postfix;
	public final int width;
	public final int height;

	private ProfileImageType(String description, String postfix, int width, int height) {
		this.description = description;
		this.postfix = postfix;
		this.width = width;
		this.height = height;
	}
}

package de.zeppelin.checkthat.webservice.controller;

public enum ReplacementLetter {
	NO_WHITESPACES(""), UNDERSCORE("_"), DOT("."), DASH("-");

	private final String text;

	private ReplacementLetter(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return this.text;
	}
}

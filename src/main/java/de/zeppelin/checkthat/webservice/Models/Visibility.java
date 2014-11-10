package de.zeppelin.checkthat.webservice.Models;

import javax.persistence.Embeddable;

@Embeddable
public enum Visibility {
	all, friends, nobody;
}

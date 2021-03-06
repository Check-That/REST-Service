package de.zeppelin.checkthat.webservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "NotFound")
public class ImageNotFoundException extends RuntimeException {

}

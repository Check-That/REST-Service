package de.zeppelin.checkthat.webservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT, reason = "UsernameAlredyExists")
public class UsernameAlreadyExistsException extends RuntimeException {

}

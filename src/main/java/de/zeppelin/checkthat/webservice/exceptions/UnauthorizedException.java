package de.zeppelin.checkthat.webservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Forbidden")
public class UnauthorizedException extends RuntimeException {

}

package edu.java.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidLinkException extends ResponseStatusException {
    public InvalidLinkException() {
        super(HttpStatus.BAD_REQUEST, "Link is not valid");
    }
}

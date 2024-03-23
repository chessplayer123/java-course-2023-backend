package edu.java.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class LinkIsNotPresentException extends ResponseStatusException {
    public LinkIsNotPresentException() {
        super(HttpStatus.NOT_FOUND, "Link is not in your track list");
    }
}

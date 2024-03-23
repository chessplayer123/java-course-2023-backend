package edu.java.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class LinkIsNotSupportedException extends ResponseStatusException {
    public LinkIsNotSupportedException() {
        super(HttpStatus.BAD_REQUEST, "Link is not supported");
    }
}

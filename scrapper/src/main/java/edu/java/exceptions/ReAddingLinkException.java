package edu.java.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ReAddingLinkException extends ResponseStatusException {
    public ReAddingLinkException() {
        super(HttpStatus.CONFLICT, "Link is already in your track list");
    }
}

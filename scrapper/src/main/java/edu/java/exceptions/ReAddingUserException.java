package edu.java.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ReAddingUserException extends ResponseStatusException {
    public ReAddingUserException() {
        super(HttpStatus.CONFLICT, "You are already registered");
    }
}

package edu.java.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ReAddingChatException extends ResponseStatusException {
    public ReAddingChatException() {
        super(HttpStatus.CONFLICT, "You are already registered");
    }
}

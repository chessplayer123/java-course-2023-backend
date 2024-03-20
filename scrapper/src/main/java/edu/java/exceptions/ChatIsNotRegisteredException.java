package edu.java.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ChatIsNotRegisteredException extends ResponseStatusException {
    public ChatIsNotRegisteredException() {
        super(HttpStatus.UNAUTHORIZED, "You are not registered");
    }
}

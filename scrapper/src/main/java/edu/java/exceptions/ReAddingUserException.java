package edu.java.exceptions;

public class ReAddingUserException extends Exception {
    public ReAddingUserException() {
        super("Attempting to re-add already registered user");
    }

    public String getDescription() {
        return "You are already registered";
    }
}

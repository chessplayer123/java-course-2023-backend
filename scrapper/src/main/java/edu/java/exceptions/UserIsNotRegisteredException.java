package edu.java.exceptions;

public class UserIsNotRegisteredException extends Exception {
    public UserIsNotRegisteredException() {
        super("User is not registered");
    }

    public String getDescription() {
        return "You are not registered";
    }
}

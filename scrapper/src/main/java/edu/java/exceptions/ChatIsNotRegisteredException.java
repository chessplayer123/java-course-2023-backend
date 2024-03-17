package edu.java.exceptions;

public class ChatIsNotRegisteredException extends Exception {
    public ChatIsNotRegisteredException() {
        super("Chat is not registered");
    }

    public String getDescription() {
        return "You are not registered";
    }
}

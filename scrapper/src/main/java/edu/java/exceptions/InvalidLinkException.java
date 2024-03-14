package edu.java.exceptions;

public class InvalidLinkException extends Exception {
    public InvalidLinkException() {
        super("Invalid link");
    }

    public String getDescription() {
        return "Link is not valid";
    }
}

package edu.java.exceptions;

public class ReAddingLinkException extends Exception {
    public ReAddingLinkException() {
        super("Attempting to re-add link to same user");
    }

    public String getDescription() {
        return "Link is already in your track list";
    }
}

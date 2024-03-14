package edu.java.exceptions;

public class LinkIsNotPresentException extends Exception {
    public LinkIsNotPresentException() {
        super("Attempting to delete not present link");
    }

    public String getDescription() {
        return "Link is not in your track list";
    }
}

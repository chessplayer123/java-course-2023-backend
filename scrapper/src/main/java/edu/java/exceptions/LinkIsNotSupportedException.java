package edu.java.exceptions;

public class LinkIsNotSupportedException extends Exception {
    public LinkIsNotSupportedException() {
        super("Attempting to track unsupported link");
    }

    public String getDescription() {
        return "Link is not supported";
    }
}

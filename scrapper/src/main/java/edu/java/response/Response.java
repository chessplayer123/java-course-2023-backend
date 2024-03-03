package edu.java.response;

public interface Response {
    String getSummary();

    default String getDifference(Response response) throws IllegalArgumentException {
        throw new IllegalArgumentException();
    }
}

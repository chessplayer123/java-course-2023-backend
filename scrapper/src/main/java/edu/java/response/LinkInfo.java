package edu.java.response;

import java.net.URI;

public interface LinkInfo {
    URI getLink();

    String getSummary();

    default String getDifference(LinkInfo response) throws IllegalArgumentException {
        throw new IllegalArgumentException();
    }
}

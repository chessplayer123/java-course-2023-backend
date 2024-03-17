package edu.java.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;

public interface LinkApiResponse {
    URI getLink();

    String getSummary();

    String serializeToJson();

    default LinkApiResponse deserializeFromJson(String jsonPayload) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonPayload, getClass());
    }

    default String retrieveEvents(LinkApiResponse response) throws IllegalArgumentException {
        throw new IllegalArgumentException();
    }
}

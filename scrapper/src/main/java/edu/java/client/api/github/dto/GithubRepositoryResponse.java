package edu.java.client.api.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.response.LinkApiResponse;
import java.net.URI;

public record GithubRepositoryResponse(
    @JsonProperty("full_name")
    String fullName,
    @JsonProperty("html_url")
    URI url
) implements LinkApiResponse {
    @Override
    public String getSummary() {
        return "Github repository '%s'".formatted(fullName);
    }
}

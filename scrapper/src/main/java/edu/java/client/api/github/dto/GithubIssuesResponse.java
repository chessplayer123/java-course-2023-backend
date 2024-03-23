package edu.java.client.api.github.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.client.api.LinkUpdateEvent;
import edu.java.response.LinkUpdateResponse;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public record GithubIssuesResponse(@JsonIgnore List<Issue> issues) implements LinkUpdateResponse {
    @JsonCreator
    public GithubIssuesResponse {
    }

    @Override
    public List<LinkUpdateEvent> pullUpdates(URI url) {
        return issues.stream()
            .map(issue -> new LinkUpdateEvent(url, issue.toString(), issue.createdAt))
            .toList();
    }

    private record Issue(String title, User user, @JsonProperty("created_at") OffsetDateTime createdAt) {
        @Override
        public String toString() {
            return "New issue by %s:\n%s".formatted(user.login, title);
        }
    }

    private record User(String login) {
    }
}

package edu.java.client.api.github.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.client.api.LinkUpdateEvent;
import edu.java.response.LinkUpdateResponse;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Getter;

public record GithubCommitsResponse(@JsonIgnore List<Commit> commits) implements LinkUpdateResponse {
    @JsonCreator
    public GithubCommitsResponse {
    }

    @Override
    public List<LinkUpdateEvent> pullUpdates(URI url) {
        return commits.stream()
            .map(commit -> new LinkUpdateEvent(url, commit.toString(), commit.getDate()))
            .toList();
    }

    @Getter
    private static class Commit {
        @JsonIgnore
        private final String message;
        @JsonIgnore
        private final String author;
        @JsonIgnore
        private final OffsetDateTime date;

        Commit(@JsonProperty("commit") CommitData data) {
            message = data.message;
            author = data.author.name;
            date = data.author.date;
        }

        @Override
        public String toString() {
            return "New commit by %s:\n%s".formatted(author, message);
        }
    }

    private record CommitData(String message, Author author) {
    }

    private record Author(String name, OffsetDateTime date) {
    }
}

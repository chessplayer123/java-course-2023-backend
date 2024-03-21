package edu.java.client.api.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.client.api.LinkUpdateEvent;
import edu.java.response.LinkUpdateResponse;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public record StackoverflowCommentsResponse(
    @JsonProperty("items")
    List<Comment> comments
) implements LinkUpdateResponse {
    @Override
    public List<LinkUpdateEvent> pullUpdates(URI url) {
        return comments.stream()
            .map(answer -> new LinkUpdateEvent(url, answer.toString(), answer.createdAt))
            .toList();
    }

    private record Comment(Owner owner, @JsonProperty("creation_date") OffsetDateTime createdAt, String body) {
        @Override
        public String toString() {
            if (body == null) {
                return "New comment by %s".formatted(owner.name);
            }
            return "New comment by %s:\n%s".formatted(owner.name, body);
        }
    }

    private record Owner(@JsonProperty("display_name") String name) {
    }
}

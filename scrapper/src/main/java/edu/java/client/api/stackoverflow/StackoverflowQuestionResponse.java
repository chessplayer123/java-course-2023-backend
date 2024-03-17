package edu.java.client.api.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.response.LinkApiResponse;
import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

public class StackoverflowQuestionResponse implements LinkApiResponse {
    private final String title;
    private final URI url;
    private final OffsetDateTime lastUpdated;

    public StackoverflowQuestionResponse(
        @JsonProperty("items")
        List<Map<String, Object>> items
    ) {
        title = (String) items.getFirst().get("title");
        url = URI.create((String) items.getFirst().get("link"));
        lastUpdated = Instant
            .ofEpochSecond((Integer) items.getFirst().get("last_activity_date"))
            .atOffset(ZoneOffset.UTC);
    }

    @Override
    public URI getLink() {
        return url;
    }

    @Override
    public String getSummary() {
        return "StackOverflow question '%s' (%s). Last updated at %s".formatted(
            title,
            url,
            lastUpdated.toString()
        );
    }

    @Override
    public String serializeToJson() {
        return """
        {
            "items": {
                "title": "%s",
                "link": "%s",
                "last_activity_date": "%s"
            }
        }""".formatted(title, url, lastUpdated);
    }

    @Override
    public String retrieveEvents(LinkApiResponse supplier) throws IllegalArgumentException {
        if (supplier.getClass() != getClass()) {
            throw new IllegalArgumentException();
        }

        StackoverflowQuestionResponse question = (StackoverflowQuestionResponse) supplier;
        if (this.equals(question)) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("StackOverflow questions changes:");

        if (!title.equals(question.title)) {
            builder.append("\n+ Title changed: '%s' -> '%s'".formatted(question.title, title));
        }

        if (!url.equals(question.url)) {
            builder.append("\n+ Url changed: '%s' -> '%s'".formatted(question.url, url));
        }

        if (!question.lastUpdated.isEqual(lastUpdated)) {
            builder.append("\n+ Question content changed at %s. Last change was at %s".formatted(
                lastUpdated,
                question.lastUpdated
            ));
        }

        return builder.toString();
    }

    @Override
    public int hashCode() {
        return title.hashCode() + url.hashCode() + lastUpdated.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (o.getClass() != StackoverflowQuestionResponse.class) {
            return false;
        }
        StackoverflowQuestionResponse otherInfo = (StackoverflowQuestionResponse) o;
        return title.equals(otherInfo.title) && url.equals(otherInfo.url) && lastUpdated.equals(otherInfo.lastUpdated);
    }
}

package edu.java.client.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.exceptions.DifferenceIsNotSupportedException;
import edu.java.link.LinkInfoSupplier;
import jakarta.annotation.Nullable;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

public class StackoverflowQuestionInfo implements LinkInfoSupplier {
    private final String title;
    private final String url;
    private final OffsetDateTime lastUpdated;

    public StackoverflowQuestionInfo(
        @JsonProperty("items")
        List<Map<String, Object>> items
    ) {
        title = (String) items.getFirst().get("title");
        url = (String) items.getFirst().get("link");
        lastUpdated = Instant
            .ofEpochSecond((Integer) items.getFirst().get("last_activity_date"))
            .atOffset(ZoneOffset.UTC);
    }

    @Override
    public String getLinkSummary() {
        return "StackOverflow question '%s' (%s). Last updated at %s".formatted(
            title,
            url,
            lastUpdated.toString()
        );
    }

    @Override
    @Nullable
    public String getDifference(LinkInfoSupplier supplier) throws DifferenceIsNotSupportedException {
        if (supplier.getClass() != StackoverflowQuestionInfo.class) {
            throw new DifferenceIsNotSupportedException();
        }

        StackoverflowQuestionInfo question = (StackoverflowQuestionInfo) supplier;
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
        } else if (o.getClass() != StackoverflowQuestionInfo.class) {
            return false;
        }
        StackoverflowQuestionInfo otherInfo = (StackoverflowQuestionInfo) o;
        return title.equals(otherInfo.title) && url.equals(otherInfo.url) && lastUpdated.equals(otherInfo.lastUpdated);
    }
}
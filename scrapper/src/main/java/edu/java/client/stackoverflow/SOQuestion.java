package edu.java.client.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.link.LinkInfoSupplier;
import jakarta.annotation.Nullable;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

public class SOQuestion implements LinkInfoSupplier {
    private final String title;
    private final String url;
    private final OffsetDateTime lastUpdated;

    public SOQuestion(
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
    @SuppressWarnings("MultipleStringLiterals")
    public String getDifference(LinkInfoSupplier supplier) {
        if (supplier.getClass() != SOQuestion.class) {
            return null;
        }

        SOQuestion question = (SOQuestion) supplier;
        if (this.equals(question)) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("StackOverflow questions changes:");

        if (!title.equals(question.title)) {
            builder.append("\n+ Title changed: '")
                .append(question.title)
                .append("' -> '")
                .append(title)
                .append("'");
        }

        if (!url.equals(question.url)) {
            builder.append("\n+ Url changed: '")
                .append(question.url)
                .append("' -> '")
                .append(url)
                .append("'");
        }

        if (!question.lastUpdated.isEqual(lastUpdated)) {
            builder.append("\n+ Question content changed at")
                .append(lastUpdated)
                .append(". Last update at ")
                .append(question.lastUpdated);
        }

        return builder.toString();
    }
}

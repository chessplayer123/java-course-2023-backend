package edu.java.client.github.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.link.LinkInfoSupplier;
import jakarta.annotation.Nullable;
import java.time.OffsetDateTime;

public record GHRepository(
    @JsonProperty("full_name")
    String fullName,
    @JsonProperty("updated_at")
    OffsetDateTime lastUpdated,
    @JsonProperty("html_url")
    String url
) implements LinkInfoSupplier<GHRepository> {
    @Override
    public String getLinkSummary() {
        return "Github repository '%s' (%s). Last updated at %s.".formatted(
            fullName,
            url,
            lastUpdated.toString()
        );
    }

    @Override
    @Nullable
    @SuppressWarnings("MultipleStringLiterals")
    public String getDifference(GHRepository repository) {
        if (this.equals(repository)) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("Repository changes:");

        if (!fullName.equals(repository.fullName)) {
            builder.append("\n+ Name changed: '")
                .append(repository.fullName).append("' -> '")
                .append(fullName).append("'");
        }

        if (!url.equals(repository.url)) {
            builder.append("\n+ Url changed: '")
                .append(repository.url).append("' -> '")
                .append(url).append("'");
        }

        if (!lastUpdated.equals(repository.lastUpdated)) {
            builder.append("\n+ Content changed at ").append(lastUpdated)
                .append(". Last change was at ").append(repository.lastUpdated);
        }

        return builder.toString();
    }
}

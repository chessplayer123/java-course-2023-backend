package edu.java.client.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.exceptions.DifferenceIsNotSupportedException;
import edu.java.link.LinkInfoSupplier;
import jakarta.annotation.Nullable;
import java.time.OffsetDateTime;

public record GithubRepository(
    @JsonProperty("full_name")
    String fullName,
    @JsonProperty("updated_at")
    OffsetDateTime lastUpdated,
    @JsonProperty("html_url")
    String url
) implements LinkInfoSupplier {
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
    public String getDifference(LinkInfoSupplier supplier) throws DifferenceIsNotSupportedException {
        if (supplier.getClass() != GithubRepository.class) {
            throw new DifferenceIsNotSupportedException();
        }

        GithubRepository repository = (GithubRepository) supplier;
        if (this.equals(repository)) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("Repository changes:");

        if (!fullName.equals(repository.fullName)) {
            builder.append("\n+ Name changed: '%s' -> '%s'".formatted(repository.fullName, fullName));
        }

        if (!url.equals(repository.url)) {
            builder.append("\n+ Url changed: '%s' -> '%s'".formatted(repository.url, url));
        }

        if (!lastUpdated.equals(repository.lastUpdated)) {
            builder.append("\n+ Content changed at %s. Last change was at %s".formatted(
                lastUpdated,
                repository.lastUpdated
            ));
        }

        return builder.toString();
    }
}

package edu.java.repository.dto;

import java.net.URI;
import java.time.OffsetDateTime;

public record Link(
    Long id,
    URI url,
    String description,
    OffsetDateTime createdAt,
    OffsetDateTime lastCheckTime
) {
    public static Link from(
        URI url,
        String description,
        OffsetDateTime createdAt,
        OffsetDateTime lastCheckTime
    ) {
        return new Link(null, url, description, createdAt, lastCheckTime);
    }
}

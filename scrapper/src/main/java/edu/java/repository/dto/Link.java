package edu.java.repository.dto;

import java.net.URI;
import java.time.OffsetDateTime;

public record Link(
    Long id,
    URI url,
    String data,
    OffsetDateTime createdAt,
    OffsetDateTime lastCheckTime
) {
}

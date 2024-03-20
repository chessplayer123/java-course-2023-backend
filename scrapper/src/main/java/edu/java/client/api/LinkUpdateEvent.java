package edu.java.client.api;

import java.net.URI;
import java.time.OffsetDateTime;

public record LinkUpdateEvent(
    URI link,
    String description,
    OffsetDateTime date
) {
}

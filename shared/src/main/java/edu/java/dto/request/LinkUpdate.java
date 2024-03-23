package edu.java.dto.request;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public record LinkUpdate(
    Long id,
    URI url,
    OffsetDateTime date,
    String description,
    List<Long> tgChatIds
) {
}

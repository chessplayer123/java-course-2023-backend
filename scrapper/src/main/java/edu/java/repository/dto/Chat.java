package edu.java.repository.dto;

import java.time.OffsetDateTime;

public record Chat(
    Long id,
    OffsetDateTime registrationTime
) {
}

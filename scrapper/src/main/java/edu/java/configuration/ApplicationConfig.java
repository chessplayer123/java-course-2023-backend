package edu.java.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    AccessType databaseAccessType,
    @NotNull
    Scheduler scheduler,
    @NotNull
    ClientApi api
) {
    public enum AccessType {
        JDBC,
        JOOQ,
    }

    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record ClientApi(
        @NotNull String githubUrl,
        @NotNull String stackoverflowUrl,
        @NotNull String botUrl
    ) {
    }
}

package edu.java.client.api.github.handlers;

import edu.java.client.api.ApiEndpoint;
import edu.java.client.api.github.dto.GithubCommitsResponse;
import edu.java.client.api.github.dto.GithubIssuesResponse;
import edu.java.client.api.github.dto.GithubRepositoryResponse;
import edu.java.response.LinkApiResponse;
import edu.java.response.LinkUpdateResponse;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class GithubRepositoryHandler extends GithubLinkHandler {
    private static final Pattern REPOSITORY_URL_PATTERN = Pattern.compile("https://github.com/.+/.+");

    @Override
    public ApiEndpoint<? extends LinkApiResponse> retrieveInfoEndpoint(URI url) {
        return ApiEndpoint
            .callTo("/repos/%s".formatted(url.getPath()))
            .andReturn(GithubRepositoryResponse.class);
    }

    @Override
    public List<ApiEndpoint<? extends LinkUpdateResponse>> retrieveUpdatesEndpoints(URI url, OffsetDateTime fromDate) {
        return List.of(
            ApiEndpoint
                .callTo("/repos/%s/commits?since=%s".formatted(url.getPath(), fromDate))
                .andReturn(GithubCommitsResponse.class),
            ApiEndpoint
                .callTo("/repos/%s/issues?since=%s".formatted(url.getPath(), fromDate))
                .andReturn(GithubIssuesResponse.class)
        );
    }

    @Override
    protected Pattern getUrlPattern() {
        return REPOSITORY_URL_PATTERN;
    }
}

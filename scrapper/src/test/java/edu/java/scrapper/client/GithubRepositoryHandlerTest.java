package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.api.LinkUpdateEvent;
import edu.java.client.api.github.GithubClient;
import edu.java.client.api.github.handlers.GithubRepositoryHandler;
import edu.java.exceptions.InvalidLinkException;
import edu.java.response.LinkApiResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GithubRepositoryHandlerTest extends AbstractTest {
    private static final WireMockServer server = new WireMockServer(wireMockConfig().dynamicPort());
    private final GithubClient githubClient = new GithubClient(server.baseUrl(), List.of(
        new GithubRepositoryHandler())
    );

    @BeforeAll
    public static void startServer() {
        server.start();
    }

    @AfterAll
    public static void stopServer() {
        server.stop();
    }

    @Test
    @SneakyThrows
    public void obtainedResponseReturnsExpectedSummaryForCorrectUrl() {
        server.stubFor(get(urlPathMatching("/repos/chessplayer123/java-course-2023-backend"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToString("src/test/resources/github/response.json"))
            )
        );

        LinkApiResponse response = githubClient.fetch(URI.create("https://github.com/chessplayer123/java-course-2023-backend"));

        String actualSummary = response.getSummary();
        String expectedSummary = "Github repository 'chessplayer123/java-course-2023-backend' (https://github.com/chessplayer123/java-course-2023-backend)";

        assertThat(actualSummary).isEqualTo(expectedSummary);
    }

    @Test
    @SneakyThrows
    public void callToInvalidUrlThrowsException() {
        server.stubFor(get(urlPathMatching("/repos/notExistentUser/notExistentRepo"))
            .willReturn(aResponse()
                .withStatus(404)
            )
        );

        assertThatThrownBy(() -> {
            githubClient.fetch(URI.create("https://github.com/notExistentUser/notExistentRepo"));
        }).isInstanceOf(InvalidLinkException.class);
    }

    @Test
    @SneakyThrows
    public void retrieveUpdatesShouldReturnExpectedEvents() {
        URI url = URI.create("https://github.com/userName/repoName");
        OffsetDateTime fromDate = OffsetDateTime.now();

        server.stubFor(
            get(urlEqualTo("/repos%s/commits?since=%s".formatted(url.getPath(), fromDate)))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToString("src/test/resources/github/commits-response.json"))
            )
        );
        server.stubFor(
            get(urlEqualTo("/repos%s/issues?since=%s".formatted(url.getPath(), fromDate)))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToString("src/test/resources/github/issues-response.json"))
            )
        );

        LinkUpdateEvent[] expectedUpdates = {
            new LinkUpdateEvent(url, "New commit by user1:\nCommit message 1", OffsetDateTime.parse("2023-12-23T11:16:41Z")),
            new LinkUpdateEvent(url, "New commit by user2:\nCommit message 2", OffsetDateTime.parse("2023-12-23T11:16:41Z")),
            new LinkUpdateEvent(url, "New issue by user1:\nIssue title 1", OffsetDateTime.parse("2024-03-20T07:32:18Z")),
            new LinkUpdateEvent(url, "New issue by user2:\nIssue title 2", OffsetDateTime.parse("2024-03-20T07:32:18Z"))
        };
        List<LinkUpdateEvent> actualUpdates = githubClient.retrieveUpdates(url, fromDate);

        assertThat(actualUpdates).containsExactlyInAnyOrder(expectedUpdates);
    }
}

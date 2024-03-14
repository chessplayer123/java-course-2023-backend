package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.api.github.GithubClient;
import edu.java.client.api.github.GithubRepositoryHandler;
import edu.java.exceptions.InvalidLinkException;
import edu.java.exceptions.LinkIsNotSupportedException;
import edu.java.response.LinkInfo;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GithubClientTest extends AbstractTest {
    private static final WireMockServer server = new WireMockServer(wireMockConfig().dynamicPort());
    private final GithubClient githubClient = new GithubClient(server.baseUrl(), List.of(new GithubRepositoryHandler()));

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
    public void obtainedSupplierReturnsExpectedSummaryForCorrectUrl() {
        server.stubFor(get(urlPathMatching("/repos/chessplayer123/java-course-2023-backend"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToString("src/test/resources/github-response1.json"))
            )
        );

        LinkInfo response = githubClient.fetch(URI.create("https://github.com/chessplayer123/java-course-2023-backend"));

        String actualSummary = response.getSummary();
        String expectedSummary = "Github repository 'chessplayer123/java-course-2023-backend' (https://github.com/chessplayer123/java-course-2023-backend). Last updated at 2023-10-14T11:23:44Z.";

        assertThat(actualSummary).isEqualTo(expectedSummary);
    }

    @Test
    @SneakyThrows
    public void suppliersDifferenceReturnsExpectedMessage() {
        server.stubFor(get(urlPathMatching("/repos/chessplayer123/java-course-2023-backend"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToString("src/test/resources/github-response1.json"))
            )
        );
        server.stubFor(get(urlPathMatching("/repos/newUserName/newRepoName"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToString("src/test/resources/github-response2.json"))
            )
        );

        LinkInfo
            prevResponse = githubClient.fetch(URI.create("https://github.com/chessplayer123/java-course-2023-backend"));
        LinkInfo newResponse = githubClient.fetch(URI.create("https://github.com/newUserName/newRepoName"));

        String actualDifference = newResponse.getDifference(prevResponse);
        String expectedDifference = """
            Repository changes:
            + Name changed: 'chessplayer123/java-course-2023-backend' -> 'newUserName/newRepoName'
            + Url changed: 'https://github.com/chessplayer123/java-course-2023-backend' -> 'https://github.com/newUserName/newRepoName'
            + Content changed at 2024-02-02T11:23:44Z. Last change was at 2023-10-14T11:23:44Z""";

        assertThat(actualDifference).isEqualTo(expectedDifference);
    }

    @Test
    @SneakyThrows
    public void sameSupplierReturnsNullDifference() {
        server.stubFor(get(urlPathMatching("/repos/chessplayer123/java-course-2023-backend"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToString("src/test/resources/github-response1.json"))
            )
        );

        LinkInfo response = githubClient.fetch(URI.create("https://github.com/chessplayer123/java-course-2023-backend"));

        assertThat(response.getDifference(response)).isNull();
    }

    @Test
    @SneakyThrows
    public void callToInvalidUrlReturnsNullSupplier() {
        server.stubFor(get(urlPathMatching("/repos/notExistentUser/notExistentRepo"))
            .willReturn(aResponse()
                .withStatus(404)
            )
        );

        assertThatThrownBy(() -> {
            githubClient.fetch(URI.create("https://github.com/notExistentUser/notExistentRepo"));
        }).isInstanceOf(InvalidLinkException.class);
    }
}

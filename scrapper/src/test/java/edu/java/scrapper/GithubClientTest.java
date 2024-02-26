package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.Client;
import edu.java.client.github.GithubClient;
import edu.java.client.github.GithubRepositorySubClient;
import edu.java.link.LinkInfoSupplier;
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

public class GithubClientTest {
    private static WireMockServer server;
    private static GithubClient githubClient;

    @BeforeAll
    public static void constructWireMockServer() {
        server = new WireMockServer(wireMockConfig().dynamicPort());

        server.stubFor(get(urlPathMatching("/repos/chessplayer123/java-course-2023-backend"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                {
                    "full_name": "chessplayer123/java-course-2023-backend",
                    "html_url": "https://github.com/chessplayer123/java-course-2023-backend",
                    "updated_at": "2023-10-14T11:23:44Z"
                }
                """)
            )
        );

        server.stubFor(get(urlPathMatching("/repos/newUserName/newRepoName"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                {
                    "full_name": "newUserName/newRepoName",
                    "html_url": "https://github.com/newUserName/newRepoName",
                    "updated_at": "2024-02-02T11:23:44Z"
                }
                """)
            )
        );

        server.stubFor(get(urlPathMatching("/repos/notExistentUser/notExistentRepo"))
            .willReturn(aResponse()
                    .withStatus(404)
            )
        );

        server.start();

        githubClient = new GithubClient(server.baseUrl(), List.of(new GithubRepositorySubClient()));
    }

    @AfterAll
    public static void destroyWireMockServer() {
        server.stop();
    }

    @Test
    @SneakyThrows
    public void obtainedSupplierReturnsExpectedSummaryForCorrectUrl() {
        LinkInfoSupplier supplier = githubClient.fetch(URI.create("https://github.com/chessplayer123/java-course-2023-backend").toURL());

        String actualSummary = supplier.getLinkSummary();
        String expectedSummary = "Github repository 'chessplayer123/java-course-2023-backend' (https://github.com/chessplayer123/java-course-2023-backend). Last updated at 2023-10-14T11:23:44Z.";

        assertThat(actualSummary).isEqualTo(expectedSummary);
    }

    @Test
    @SneakyThrows
    public void suppliersDifferenceReturnsExpectedMessage() {
        LinkInfoSupplier prevSupplier = githubClient.fetch(URI.create("https://github.com/chessplayer123/java-course-2023-backend").toURL());
        LinkInfoSupplier newSupplier = githubClient.fetch(URI.create("https://github.com/newUserName/newRepoName").toURL());

        String actualDifference = newSupplier.getDifference(prevSupplier);
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
        LinkInfoSupplier supplier = githubClient.fetch(URI.create("https://github.com/chessplayer123/java-course-2023-backend").toURL());

        assertThat(supplier.getDifference(supplier)).isNull();
    }

    @Test
    @SneakyThrows
    public void callToInvalidUrlReturnsNullSupplier() {
        LinkInfoSupplier supplier = githubClient.fetch(URI.create("https://github.com/notExistentUser/notExistentRepo").toURL());

        assertThat(supplier).isNull();
    }
}

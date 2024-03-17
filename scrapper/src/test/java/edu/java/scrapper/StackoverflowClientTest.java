package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.api.stackoverflow.StackoverflowClient;
import edu.java.client.api.stackoverflow.StackoverflowQuestionHandler;
import edu.java.response.LinkApiResponse;
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

public class StackoverflowClientTest extends AbstractTest {
    private static final WireMockServer server = new WireMockServer(wireMockConfig().dynamicPort());
    private final StackoverflowClient stackoverflowClient = new StackoverflowClient(server.baseUrl(), List.of(new StackoverflowQuestionHandler()));

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
        server.stubFor(get(urlPathMatching("/questions/32126613"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToString("src/test/resources/stackoverflow-response1.json"))
            )
        );

        LinkApiResponse response = stackoverflowClient.fetch(URI.create(
            "https://stackoverflow.com/questions/32126613/c-equivalent-of-rusts-resultt-e-type"
        ));

        String actualSummary = response.getSummary();
        String expectedSummary = "StackOverflow question 'C++ equivalent of Rust's Result<T, E> type?' " +
            "(https://stackoverflow.com/questions/32126613/c-equivalent-of-rusts-resultt-e-type). " +
            "Last updated at 2023-02-06T15:34:55Z";

        assertThat(actualSummary).isEqualTo(expectedSummary);
    }

    @Test
    @SneakyThrows
    public void suppliersDifferenceReturnsExpectedMessage() {
        server.stubFor(get(urlPathMatching("/questions/32126613"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToString("src/test/resources/stackoverflow-response1.json"))
            )
        );
        server.stubFor(get(urlPathMatching("/questions/56016409"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToString("src/test/resources/stackoverflow-response2.json"))
            )
        );

        LinkApiResponse prevResponse = stackoverflowClient.fetch(URI.create(
            "https://stackoverflow.com/questions/32126613/c-equivalent-of-rusts-resultt-e-type"
        ));
        LinkApiResponse newResponse = stackoverflowClient.fetch(URI.create(
            "https://stackoverflow.com/questions/56016409/how-to-exclude-certain-classes-from-being-included-in-the-code-coverage-java"
        ));

        String actualDifference = newResponse.retrieveEvents(prevResponse);
        String expectedDifference = """
            StackOverflow questions changes:
            + Title changed: 'C++ equivalent of Rust's Result<T, E> type?' -> 'How to exclude certain classes from being included in the code coverage? (Java)'
            + Url changed: 'https://stackoverflow.com/questions/32126613/c-equivalent-of-rusts-resultt-e-type' -> 'https://stackoverflow.com/questions/56016409/how-to-exclude-certain-classes-from-being-included-in-the-code-coverage-java'
            + Question content changed at 2023-09-05T18:07:30Z. Last change was at 2023-02-06T15:34:55Z""";

        assertThat(actualDifference).isEqualTo(expectedDifference);
    }
}

package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.stackoverflow.StackoverflowClient;
import edu.java.client.stackoverflow.StackoverflowQuestionSubClient;
import edu.java.link.LinkInfoSupplier;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

public class StackoverflowClientTest extends AbstractTest {
    @Test
    @SneakyThrows
    public void obtainedSupplierReturnsExpectedSummaryForCorrectUrl() {
        WireMockServer server = new WireMockServer(wireMockConfig().dynamicPort());
        server.stubFor(get(urlPathMatching("/questions/32126613"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToString("src/test/resources/stackoverflow-response1.json"))
            )
        );
        server.start();
        StackoverflowClient stackOverflowClient = new StackoverflowClient(server.baseUrl(), List.of(new StackoverflowQuestionSubClient()));

        LinkInfoSupplier supplier = stackOverflowClient.fetch(URI.create(
            "https://stackoverflow.com/questions/32126613/c-equivalent-of-rusts-resultt-e-type"
        ).toURL());

        String actualSummary = supplier.getLinkSummary();
        String expectedSummary = "StackOverflow question 'C++ equivalent of Rust's Result<T, E> type?' " +
            "(https://stackoverflow.com/questions/32126613/c-equivalent-of-rusts-resultt-e-type). " +
            "Last updated at 2023-02-06T15:34:55Z";

        assertThat(actualSummary).isEqualTo(expectedSummary);
    }

    @Test
    @SneakyThrows
    public void suppliersDifferenceReturnsExpectedMessage() {
        WireMockServer server = new WireMockServer(wireMockConfig().dynamicPort());
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
        server.start();
        StackoverflowClient stackOverflowClient = new StackoverflowClient(server.baseUrl(), List.of(new StackoverflowQuestionSubClient()));

        LinkInfoSupplier prevSupplier = stackOverflowClient.fetch(URI.create(
            "https://stackoverflow.com/questions/32126613/c-equivalent-of-rusts-resultt-e-type"
        ).toURL());
        LinkInfoSupplier newSupplier = stackOverflowClient.fetch(URI.create(
            "https://stackoverflow.com/questions/56016409/how-to-exclude-certain-classes-from-being-included-in-the-code-coverage-java"
        ).toURL());

        String actualDifference = newSupplier.getDifference(prevSupplier);
        String expectedDifference = """
            StackOverflow questions changes:
            + Title changed: 'C++ equivalent of Rust's Result<T, E> type?' -> 'How to exclude certain classes from being included in the code coverage? (Java)'
            + Url changed: 'https://stackoverflow.com/questions/32126613/c-equivalent-of-rusts-resultt-e-type' -> 'https://stackoverflow.com/questions/56016409/how-to-exclude-certain-classes-from-being-included-in-the-code-coverage-java'
            + Question content changed at 2023-09-05T18:07:30Z. Last change was at 2023-02-06T15:34:55Z""";

        assertThat(actualDifference).isEqualTo(expectedDifference);
    }
}
package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.Client;
import edu.java.client.github.repository.GHRepositoryClient;
import edu.java.client.stackoverflow.question.SOQuestionClient;
import edu.java.link.LinkInfoSupplier;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.net.MalformedURLException;
import java.net.URI;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

public class StackOverflowQuestionClientTest {
    private static WireMockServer server;

    @BeforeAll
    public static void constructWireMockServer() {
        server = new WireMockServer(wireMockConfig().dynamicPort());

        server.stubFor(get(urlPathMatching("/questions/32126613"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                {
                    "items": [
                        {
                            "title": "C++ equivalent of Rust's Result<T, E> type?",
                            "link": "https://stackoverflow.com/questions/32126613/c-equivalent-of-rusts-resultt-e-type",
                            "last_activity_date": 1675697695
                        }
                    ]
                }
                """)
            )
        );

        server.stubFor(get(urlPathMatching("/questions/56016409"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                {
                    "items": [
                        {
                            "title": "How to exclude certain classes from being included in the code coverage? (Java)",
                            "link": "https://stackoverflow.com/questions/56016409/how-to-exclude-certain-classes-from-being-included-in-the-code-coverage-java",
                            "last_activity_date": 1693937250
                        }
                    ]
                }
                """)
            )
        );

        server.start();
    }

    @AfterAll
    public static void destroyWireMockServer() {
        server.stop();
    }

    @Test
    public void obtainedSupplierReturnsExpectedSummaryForCorrectUrl() throws MalformedURLException {
        Client client = new SOQuestionClient(server.baseUrl());
        LinkInfoSupplier supplier = client.fetch(URI.create(
            "https://stackoverflow.com/questions/32126613/c-equivalent-of-rusts-resultt-e-type"
        ).toURL());

        String actualSummary = supplier.getLinkSummary();
        String expectedSummary = "StackOverflow question 'C++ equivalent of Rust's Result<T, E> type?' " +
            "(https://stackoverflow.com/questions/32126613/c-equivalent-of-rusts-resultt-e-type). " +
            "Last updated at 2023-02-06T15:34:55Z";

        assertThat(actualSummary).isEqualTo(expectedSummary);
    }

    @Test
    public void suppliersDifferenceReturnsExpectedMessage() throws MalformedURLException {
        Client client = new SOQuestionClient(server.baseUrl());
        LinkInfoSupplier prevSupplier = client.fetch(URI.create(
            "https://stackoverflow.com/questions/32126613/c-equivalent-of-rusts-resultt-e-type"
        ).toURL());
        LinkInfoSupplier newSupplier = client.fetch(URI.create(
            "https://stackoverflow.com/questions/56016409/how-to-exclude-certain-classes-from-being-included-in-the-code-coverage-java"
        ).toURL());

        String actualDifference = newSupplier.getDifference(prevSupplier);
        String expectedDifference = """
            StackOverflow questions changes:
            + Title changed: 'C++ equivalent of Rust's Result<T, E> type?' -> 'How to exclude certain classes from being included in the code coverage? (Java)'
            + Url changed: 'https://stackoverflow.com/questions/32126613/c-equivalent-of-rusts-resultt-e-type' -> 'https://stackoverflow.com/questions/56016409/how-to-exclude-certain-classes-from-being-included-in-the-code-coverage-java'
            + Question content changed at2023-09-05T18:07:30Z. Last update at 2023-02-06T15:34:55Z""";

        assertThat(actualDifference).isEqualTo(expectedDifference);
    }
}

package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.api.LinkUpdateEvent;
import edu.java.client.api.stackoverflow.StackoverflowClient;
import edu.java.client.api.stackoverflow.handlers.StackoverflowQuestionHandler;
import edu.java.response.LinkApiResponse;
import lombok.SneakyThrows;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.forbidden;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

public class StackoverflowQuestionHandlerTest extends AbstractTest {
    private static final WireMockServer server = new WireMockServer(wireMockConfig().dynamicPort());
    private final StackoverflowClient stackoverflowClient = new StackoverflowClient(server.baseUrl(), List.of(
        new StackoverflowQuestionHandler())
    );

    @BeforeAll
    public static void startServer() {
        server.start();
    }

    @AfterAll
    public static void stopServer() {
        server.stop();
    }

    private OffsetDateTime epochToOffsetDateTime(Long epochSeconds) {
        return OffsetDateTime.of(LocalDateTime.ofEpochSecond(epochSeconds, 0, ZoneOffset.UTC), ZoneOffset.UTC);
    }

    @Test
    @SneakyThrows
    public void obtainedResponseReturnsExpectedSummaryForCorrectUrl() {
        server.stubFor(get(urlPathMatching("/questions/32126613"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToString("src/test/resources/stackoverflow/stackoverflow-response.json"))
            )
        );

        LinkApiResponse response = stackoverflowClient.fetch(URI.create(
            "https://stackoverflow.com/questions/32126613/c-equivalent-of-rusts-resultt-e-type"
        ));

        String actualSummary = response.getSummary();
        String expectedSummary = "StackOverflow question: 'C++ equivalent of Rust's Result<T, E> type?'";

        assertThat(actualSummary).isEqualTo(expectedSummary);
    }

    @Test
    @SneakyThrows
    public void retrieveUpdatesShouldReturnExpectedEvents() {
        URI url = URI.create("https://stackoverflow.com/questions/44593066/spring-webflux-webclient-get-body-on-error");
        OffsetDateTime fromDate = epochToOffsetDateTime(1582207100L);

        server.stubFor(
            get(urlPathMatching("/questions/44593066/answers"))
            .withQueryParam("filter", equalTo("withbody"))
            .withQueryParam("site", equalTo("stackoverflow"))
            .withQueryParam("fromDate", equalTo(String.valueOf(fromDate.toEpochSecond())))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToString("src/test/resources/stackoverflow/answers-response.json"))
            )
        );
        server.stubFor(
            get(urlPathMatching("/questions/44593066/comments"))
            .withQueryParam("filter", equalTo("withbody"))
            .withQueryParam("site", equalTo("stackoverflow"))
            .withQueryParam("fromDate", equalTo(String.valueOf(fromDate.toEpochSecond())))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToString("src/test/resources/stackoverflow/comments-response.json"))
            )
        );

        LinkUpdateEvent[] expectedUpdates = {
            new LinkUpdateEvent(url, "New answer by user_name1:\nAnswer content", epochToOffsetDateTime(1692207100L)),
            new LinkUpdateEvent(url, "New answer by user_name2", epochToOffsetDateTime(1592207100L)),
            new LinkUpdateEvent(url, "New comment by user_name3:\nComment content", epochToOffsetDateTime(1628754641L)),
            new LinkUpdateEvent(url, "New comment by user_name4", epochToOffsetDateTime(1692207100L)),
        };
        List<LinkUpdateEvent> actualUpdates = stackoverflowClient.retrieveUpdates(url, fromDate);

        assertThat(actualUpdates).containsExactlyInAnyOrder(expectedUpdates);
    }
}

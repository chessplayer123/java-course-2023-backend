package edu.java.bot;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.exceptions.CommandException;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinkResponse;
import io.swagger.v3.oas.annotations.links.Link;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ScrapperClientTest {
    private static final WireMockServer server = new WireMockServer(wireMockConfig().dynamicPort());
    private final ScrapperClient client = new ScrapperClient(server.baseUrl());

    private static String getApiErrorResponseBody(String description) {
        return """
        {
            "description": "%s",
            "code": "400",
            "exceptionName": "ApiError",
            "exceptionMessage": "Exception message",
            "stacktrace": ["string"]
        }
        """.formatted(description);
    }

    @BeforeAll
    public static void startServer() {
        server.start();
    }

    @AfterAll
    public static void stopServer() {
        server.stop();
    }

    @Test
    public void registerChatShouldNotThrowExceptionOnSuccessResponse() {
        server.stubFor(post(urlPathMatching("/tg-chat/1"))
            .willReturn(ok())
        );

        assertThatCode(() -> client.registerChat(1L))
            .doesNotThrowAnyException();
    }

    @Test
    public void registerChatShouldThrowCommandExceptionOnErrorResponse() {
        String expectedExceptionMessage = "Expected message";
        server.stubFor(post(urlPathMatching("/tg-chat/10"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader("Content-Type", "application/json")
                .withBody(getApiErrorResponseBody(expectedExceptionMessage))
            )
        );

        assertThatThrownBy(() -> client.registerChat(10L))
            .isInstanceOf(CommandException.class)
            .hasMessage(expectedExceptionMessage);
    }

    @Test
    public void unregisterChatShouldNotThrowExceptionOnSuccessResponse() {
        server.stubFor(delete(urlPathMatching("/tg-chat/1"))
            .willReturn(ok())
        );

        assertThatCode(() -> client.unregisterChat(1L))
            .doesNotThrowAnyException();
    }

    @Test
    public void unregisterChatShouldThrowCommandExceptionOnErrorResponse() {
        String expectedExceptionMessage = "Expected message";
        server.stubFor(delete(urlPathMatching("/tg-chat/31415926"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader("Content-Type", "application/json")
                .withBody(getApiErrorResponseBody(expectedExceptionMessage))
            )
        );

        assertThatThrownBy(() -> client.unregisterChat(31415926L))
            .isInstanceOf(CommandException.class)
            .hasMessage(expectedExceptionMessage);
    }

    @Test
    public void addLinkShouldReturnExpectedResponse() {
        server.stubFor(post(urlPathMatching("/links"))
            .withHeader("Tg-Chat-Id", equalTo("0"))
            .withRequestBody(equalToJson("{\"link\": \"https://github.com\"}"))
            .willReturn(ok()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                {
                    "id": 1,
                    "url": "https://github.com",
                    "description": "text"
                }
                """)
            )
        );

        LinkResponse actualResponse = client.addLink(0L, "https://github.com");
        LinkResponse expectedResponse = new LinkResponse(1L, URI.create("https://github.com"), "text");

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void addLinkShouldThrowCommandExceptionOnErrorResponse() {
        String expectedExceptionMessage = "Expected message";
        server.stubFor(post(urlPathMatching("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .withRequestBody(equalToJson("{\"link\": \"https://github.com\"}"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader("Content-Type", "application/json")
                .withBody(getApiErrorResponseBody(expectedExceptionMessage))
            )
        );

        assertThatThrownBy(() -> client.addLink(1L, "https://github.com"))
            .isInstanceOf(CommandException.class)
            .hasMessage(expectedExceptionMessage);
    }

    @Test
    public void deleteLinkShouldReturnExpectedResponse() {
        server.stubFor(delete(urlPathMatching("/links"))
            .withHeader("Tg-Chat-Id", equalTo("0"))
            .withRequestBody(equalToJson("{\"link\": \"https://github.com\"}"))
            .willReturn(ok()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                {
                    "id": 123,
                    "url": "https://github.com",
                    "description": "link info"
                }
                """)
            )
        );

        LinkResponse actualResponse = client.deleteLink(0L, "https://github.com");
        LinkResponse expectedResponse = new LinkResponse(123L, URI.create("https://github.com"), "link info");

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void deleteLinkShouldThrowCommandExceptionOnErrorResponse() {
        String expectedExceptionMessage = "Expected message";
        server.stubFor(delete(urlPathMatching("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .withRequestBody(equalToJson("{\"link\": \"https://github.com\"}"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader("Content-Type", "application/json")
                .withBody(getApiErrorResponseBody(expectedExceptionMessage))
            )
        );

        assertThatThrownBy(() -> client.deleteLink(1L, "https://github.com"))
            .isInstanceOf(CommandException.class)
            .hasMessage(expectedExceptionMessage);
    }

    @Test
    public void listLinksShouldReturnExpectedLinks() {
        server.stubFor(get(urlPathMatching("/links"))
            .withHeader("Tg-Chat-Id", equalTo("0"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                {
                    "links": [
                        {"id": 0, "url": "https://github.com", "description": "link info1"},
                        {"id": 1, "url": "https://stackoverflow.com", "description": "link info2"}
                    ],
                    "size": 2
                }
                """)
            )
        );

        ListLinkResponse actualResponse = client.listLinks(0L);
        ListLinkResponse expectedResponse = new ListLinkResponse(
            List.of(
                new LinkResponse(0L, URI.create("https://github.com"), "link info1"),
                new LinkResponse(1L, URI.create("https://stackoverflow.com"), "link info2")
            ),
            2
        );

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void listLinksShouldThrowCommandExceptionOnErrorResponse() {
        String expectedExceptionMessage = "Expected message";
        server.stubFor(get(urlPathMatching("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader("Content-Type", "application/json")
                .withBody(getApiErrorResponseBody(expectedExceptionMessage))
            )
        );

        assertThatThrownBy(() -> client.listLinks(1L))
            .isInstanceOf(CommandException.class)
            .hasMessage(expectedExceptionMessage);
    }
}

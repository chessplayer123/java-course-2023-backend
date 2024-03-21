package edu.java.client.api;

import edu.java.exceptions.InvalidLinkException;
import edu.java.exceptions.LinkIsNotSupportedException;
import edu.java.handlers.LinkHandler;
import edu.java.response.LinkApiResponse;
import edu.java.response.LinkUpdateResponse;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

public abstract class ApiClient {
    private final WebClient webClient;
    private final List<? extends LinkHandler> handlers;

    public ApiClient(String url, List<? extends LinkHandler> handlers) {
        webClient = WebClient.create(url);
        this.handlers = handlers;
    }

    private <T> T executeRequest(ApiEndpoint<T> endpoint) throws InvalidLinkException {
        try {
            return webClient
                .get()
                .uri(builder -> builder
                    .path(endpoint.path())
                    .queryParams(endpoint.queryParams())
                    .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(endpoint.responseType())
                .block();
        } catch (WebClientException err) {
            throw new InvalidLinkException();
        }
    }

    private LinkHandler findHandler(URI url) throws LinkIsNotSupportedException {
        return handlers.stream()
            .filter(handler -> handler.supports(url))
            .findAny()
            .orElseThrow(LinkIsNotSupportedException::new);
    }

    public boolean supports(URI url) {
        return handlers.stream()
            .anyMatch(handler -> handler.supports(url));
    }

    public List<LinkUpdateEvent> retrieveUpdates(
        URI url,
        OffsetDateTime fromDate
    ) throws InvalidLinkException, LinkIsNotSupportedException {
        LinkHandler handler = findHandler(url);
        var endpoints = handler.retrieveUpdatesEndpoints(url, fromDate);
        List<LinkUpdateEvent> updates = new ArrayList<>();

        for (var endpoint : endpoints) {
            LinkUpdateResponse response = executeRequest(endpoint);
            updates.addAll(response.pullUpdates(url));
        }
        return updates;
    }

    public LinkApiResponse fetch(URI url) throws InvalidLinkException, LinkIsNotSupportedException {
        LinkHandler handler = findHandler(url);
        var infoEndpoint = handler.retrieveInfoEndpoint(url);
        return executeRequest(infoEndpoint);
    }
}

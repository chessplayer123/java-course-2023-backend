package edu.java.client.api;

import edu.java.exceptions.InvalidLinkException;
import edu.java.handlers.LinkHandler;
import edu.java.response.LinkApiResponse;
import java.net.URI;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

public abstract class ApiClient {
    private final WebClient webClient;
    private final List<? extends LinkHandler> handlers;

    public ApiClient(String url, List<? extends LinkHandler> subClients) {
        webClient = WebClient.create(url);
        this.handlers = subClients;
    }

    private <T extends LinkApiResponse> T executeRequest(
        String endpoint,
        Class<T> responseClass
    ) throws InvalidLinkException {
        try {
            return webClient
                .get()
                .uri(endpoint)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(responseClass)
                .block();
        } catch (WebClientException err) {
            throw new InvalidLinkException();
        }
    }

    public <T> LinkApiResponse fetch(URI url) throws InvalidLinkException {
        for (LinkHandler handler : handlers) {
            if (!handler.supports(url)) {
                continue;
            }

            return executeRequest(
                handler.convertUrlToApiPath(url),
                handler.getResponseType()
            );
        }
        return null;
    }

    public boolean supports(URI url) {
        return handlers.stream()
            .anyMatch(handler -> handler.supports(url));
    }
}

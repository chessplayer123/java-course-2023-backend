package edu.java.client;

import edu.java.response.Response;
import java.net.URL;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

public abstract class Client {
    private final WebClient webClient;
    private final List<? extends LinkHandler> handlers;

    public Client(String url, List<? extends LinkHandler> subClients) {
        webClient = WebClient.create(url);
        this.handlers = subClients;
    }

    private <T extends Response> T executeRequest(String endpoint, Class<T> responseClass) {
        try {
            return webClient
                .get()
                .uri(endpoint)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(responseClass)
                .block();
        } catch (WebClientException err) {
            return null;
        }
    }

    public <T> Response fetch(URL url) {
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
}

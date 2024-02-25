package edu.java.client;

import edu.java.link.LinkInfoSupplier;
import jakarta.annotation.Nullable;
import java.net.URL;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

public abstract class Client {
    private final WebClient webClient;

    public Client(String url) {
        webClient = WebClient.create(url);
    }

    protected <T> T sendRequest(String endpoint, Class<T> data) {
        try {
            return webClient
                .get()
                .uri(endpoint)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(data)
                .block();
        } catch (WebClientException err) {
            return null;
        }
    }

    @Nullable
    public abstract LinkInfoSupplier fetch(URL url);

    public abstract boolean supports(URL url);
}

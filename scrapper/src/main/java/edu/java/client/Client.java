package edu.java.client;

import edu.java.link.LinkInfoSupplier;
import jakarta.annotation.Nullable;
import java.net.URL;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

public class Client {
    private final WebClient webClient;
    private final List<SubClient> subClients;

    public Client(String url, List<SubClient> subClients) {
        webClient = WebClient.create(url);
        this.subClients = subClients;
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
    public LinkInfoSupplier fetch(URL url) {
        for (SubClient subClient : subClients) {
            if (!subClient.supports(url)) {
                continue;
            }
            return sendRequest(
                subClient.getApiPath(url),
                subClient.getInfoSupplierType()
            );
        }
        return null;
    }

    public boolean supports(URL url) {
        return subClients.stream()
            .anyMatch(subClient -> subClient.supports(url));
    }
}

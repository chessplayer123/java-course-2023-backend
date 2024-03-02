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
    private final List<? extends SubClient> subClients;

    public Client(String url, List<? extends SubClient> subClients) {
        webClient = WebClient.create(url);
        this.subClients = subClients;
    }

    private <T> T sendRequest(String endpoint, Class<T> data) {
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
                subClient.convertUrlToApiPath(url),
                subClient.getInfoSupplierType()
            );
        }
        return null;
    }
}

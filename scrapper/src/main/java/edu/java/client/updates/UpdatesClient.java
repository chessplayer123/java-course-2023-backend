package edu.java.client.updates;

import edu.java.dto.request.LinkUpdate;
import edu.java.dto.response.ApiErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class UpdatesClient {
    private final WebClient webClient;

    public UpdatesClient(String botUrl) {
        webClient = WebClient.create(botUrl);
    }

    public void sendLinkUpdate(LinkUpdate update) {
        webClient
            .post()
            .uri("/updates")
            .bodyValue(update)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(ApiErrorResponse.class)
            .block();
    }
}

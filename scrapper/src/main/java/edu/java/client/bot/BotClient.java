package edu.java.client.bot;

import edu.java.dto.request.LinkUpdate;
import edu.java.dto.response.ApiErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class BotClient {
    private final WebClient webClient;

    public BotClient(String botUrl) {
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

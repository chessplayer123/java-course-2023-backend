package edu.java.client.updates;

import edu.java.dto.request.LinkUpdate;
import edu.java.dto.response.ApiErrorResponse;
import edu.java.exceptions.LinkUpdateException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
            .onStatus(HttpStatusCode::isError, response -> response
                    .bodyToMono(ApiErrorResponse.class)
                    .onErrorMap(error -> new LinkUpdateException(error.getMessage()))
                    .flatMap(body -> Mono.error(new LinkUpdateException(body.description())))
            )
            .toBodilessEntity()
            .block();
    }
}

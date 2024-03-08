package edu.java.bot.client.scrapper;

import edu.java.bot.exceptions.CommandException;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.request.TrackLinkRequest;
import edu.java.dto.response.ApiErrorResponse;
import edu.java.dto.response.ListLinkResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SuppressWarnings("MultipleStringLiterals")
public class ScrapperClient {
    private final WebClient webClient;

    public ScrapperClient(String url) {
        webClient = WebClient.create(url);
    }

    private Mono<CommandException> handleApiError(ClientResponse response) {
        ApiErrorResponse apiError = response.bodyToMono(ApiErrorResponse.class).block();
        return Mono.error(new CommandException(apiError.description()));
    }

    public void registerChat(Long chatId) {
        webClient
            .post()
            .uri("/tg-chat/%d".formatted(chatId))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::handleApiError)
            .toBodilessEntity()
            .block();
    }

    public void unregisterChat(Long chatId) {
        webClient
            .delete()
            .uri("/tg-chat/%d".formatted(chatId))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::handleApiError)
            .toBodilessEntity()
            .block();
    }

    public void addLink(Long chatId, String link) {
        webClient
            .post()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .bodyValue(new TrackLinkRequest(link))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::handleApiError)
            .toBodilessEntity()
            .block();
    }

    public void deleteLink(Long chatId, String link) {
        webClient
            .method(HttpMethod.DELETE)
            .uri("/links")
            .bodyValue(new RemoveLinkRequest(link))
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::handleApiError)
            .toBodilessEntity()
            .block();
    }

    public ListLinkResponse listLinks(Long chatId) {
        return webClient
            .get()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::isError, this::handleApiError)
            .bodyToMono(ListLinkResponse.class)
            .block();
    }
}

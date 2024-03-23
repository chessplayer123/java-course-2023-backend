package edu.java.bot.client.scrapper;

import edu.java.bot.exceptions.CommandException;
import edu.java.dto.request.TrackLinkRequest;
import edu.java.dto.request.UntrackLinkRequest;
import edu.java.dto.response.ApiErrorResponse;
import edu.java.dto.response.LinkResponse;
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

    private Mono<CommandException> handleApiError(ClientResponse response)  {
        return response
            .bodyToMono(ApiErrorResponse.class)
            .onErrorMap(error -> new CommandException(error.getMessage()))
            .flatMap(body -> Mono.error(new CommandException(body.description())));
    }

    public void registerChat(Long chatId) throws CommandException {
        webClient
            .post()
            .uri("/tg-chat/{chatId}", chatId)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::handleApiError)
            .toBodilessEntity()
            .block();
    }

    public void unregisterChat(Long chatId) {
        webClient
            .delete()
            .uri("/tg-chat/{chatId}", chatId)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::handleApiError)
            .toBodilessEntity()
            .block();
    }

    public LinkResponse addLink(Long chatId, String link) {
        return webClient
            .post()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .bodyValue(new TrackLinkRequest(link))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::handleApiError)
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public LinkResponse deleteLink(Long chatId, String link) {
        return webClient
            .method(HttpMethod.DELETE)
            .uri("/links")
            .bodyValue(new UntrackLinkRequest(link))
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::handleApiError)
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public ListLinkResponse listLinks(Long chatId) {
        return webClient
            .get()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::handleApiError)
            .bodyToMono(ListLinkResponse.class)
            .block();
    }
}

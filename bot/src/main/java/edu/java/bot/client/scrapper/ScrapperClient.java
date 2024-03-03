package edu.java.bot.client.scrapper;

import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.request.TrackLinkRequest;
import edu.java.dto.response.ApiErrorResponse;
import edu.java.dto.response.ListLinkResponse;
import java.net.MalformedURLException;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@SuppressWarnings("MultipleStringLiterals")
public class ScrapperClient {
    private final WebClient webClient;

    public ScrapperClient(String url) {
        webClient = WebClient.create(url);
    }

    public void registerChat(Long chatId) throws MalformedURLException {
        webClient
            .post()
            .uri("/tg-chat/%d".formatted(chatId))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(ApiErrorResponse.class)
            .block();
    }

    public void deleteChat(Long chatId) {
        webClient
            .delete()
            .uri("/tg-chat/%d".formatted(chatId))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(ApiErrorResponse.class)
            .block();
    }

    public void addLink(Long chatId, String link) {
        webClient
            .post()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .bodyValue(new TrackLinkRequest(link))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve();
            //.bodyToMono()
            //.block();
    }

    public void deleteLink(Long chatId, String link) {
        webClient
            .method(HttpMethod.DELETE)
            .uri("/links")
            .bodyValue(new RemoveLinkRequest(link))
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve();
            //.bodyToMono()
            //.block();
    }

    public ListLinkResponse listLinks(Long chatId) {
        return webClient
            .get()
            .uri("/links")
            .header("Tg-Chat-Id", String.valueOf(chatId))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(ListLinkResponse.class)
            .block();
    }
}

package edu.java.bot.service;

import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.exceptions.CommandException;
import edu.java.dto.response.LinkResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;

@Service
@RequiredArgsConstructor
public class ScrapperUserService implements UserService {
    private static final CommandException SERVICE_IS_UNAVAILABLE = new CommandException(
        "The bot is temporary unavailable"
    );

    private final ScrapperClient client;

    @Override
    public void registerChat(Long chatId) throws CommandException {
        try {
            client.registerChat(chatId);
        } catch (WebClientRequestException e) {
            throw SERVICE_IS_UNAVAILABLE;
        }
    }

    @Override
    public void unregisterChat(Long chatId) throws CommandException {
        try {
            client.unregisterChat(chatId);
        } catch (WebClientRequestException e) {
            throw SERVICE_IS_UNAVAILABLE;
        }
    }

    @Override
    public List<LinkResponse> getTrackedLinks(Long chatId) throws CommandException {
        try {
            return client.listLinks(chatId).links();
        } catch (WebClientRequestException e) {
            throw SERVICE_IS_UNAVAILABLE;
        }
    }

    @Override
    public void trackLink(Long chatId, String link) throws CommandException {
        try {
            client.addLink(chatId, link);
        } catch (WebClientRequestException e) {
            throw SERVICE_IS_UNAVAILABLE;
        }
    }

    @Override
    public void unTrackLink(Long chatId, String link) throws CommandException {
        try {
            client.deleteLink(chatId, link);
        } catch (WebClientRequestException e) {
            throw SERVICE_IS_UNAVAILABLE;
        }
    }
}

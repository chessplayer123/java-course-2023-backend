package edu.java.bot.service;

import edu.java.bot.exceptions.CommandException;
import edu.java.dto.response.LinkResponse;
import java.util.List;

public interface UserService {
    void registerChat(Long chatId) throws CommandException;

    void unregisterChat(Long chatId) throws CommandException;

    List<LinkResponse> getTrackedLinks(Long chatId) throws CommandException;

    void trackLink(Long chatId, String link) throws CommandException;

    void untrackLink(Long chatId, String link) throws CommandException;
}

package edu.java.bot.service;

import edu.java.bot.exceptions.TgChatBotException;
import edu.java.dto.request.LinkUpdate;

public interface NotificationService {
    void sendNotifications(LinkUpdate update) throws TgChatBotException;
}

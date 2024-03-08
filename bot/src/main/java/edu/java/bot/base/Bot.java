package edu.java.bot.base;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.exceptions.TgChatBotException;
import java.util.List;

public interface Bot extends AutoCloseable {
    void start();

    int handleUpdates(List<Update> updates);

    void sendMessage(Long chatId, String message) throws TgChatBotException;

    default void sendNotifications(List<Long> chatIds, String message) throws TgChatBotException {
        for (Long id : chatIds) {
            sendMessage(id, message);
        }
    }
}

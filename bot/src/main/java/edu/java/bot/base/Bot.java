package edu.java.bot.base;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.exceptions.TgChatBotException;
import java.util.List;

public interface Bot extends AutoCloseable {
    void start();

    int handleUpdates(List<Update> updates);

    void sendMessage(SendMessage message) throws TgChatBotException;
}

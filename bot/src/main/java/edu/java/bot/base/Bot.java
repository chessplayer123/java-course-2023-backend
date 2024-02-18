package edu.java.bot.base;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.processor.ChatProcessor;
import java.util.List;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class Bot implements AutoCloseable {
    private final TelegramBot bot;
    protected final ChatProcessor chatProcessor;

    protected Bot(String token, ChatProcessor chatProcessor) {
        bot = new TelegramBot(token);
        this.chatProcessor = chatProcessor;
    }

    protected abstract void setup();

    public int handleUpdates(List<Update> updates) {
        for (Update update : updates) {
            SendResponse response = bot.execute(chatProcessor.process(update));
            if (!response.isOk()) {
                log.warn(
                    "Error occurred [code=%d], while trying to process '%s'"
                        .formatted(response.errorCode(), update.message().text())
                );
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public void start() {
        bot.execute(new SetMyCommands(chatProcessor.getBotCommands()));
        bot.setUpdatesListener(this::handleUpdates);

        log.info("Bot was successfully started");
    }

    @Override
    public void close() {
        bot.shutdown();

        log.info("Bot was successfully stopped");
    }
}

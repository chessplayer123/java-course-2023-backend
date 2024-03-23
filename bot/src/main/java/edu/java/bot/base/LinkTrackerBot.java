package edu.java.bot.base;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.exceptions.TgChatBotException;
import edu.java.bot.processor.ChatProcessor;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class LinkTrackerBot implements Bot {
    private final TelegramBot telegramBot;
    private final ChatProcessor chatProcessor;

    @Override
    public int handleUpdates(List<Update> updates) {
        for (Update update : updates) {
            SendResponse response = telegramBot.execute(chatProcessor.process(update));
            if (!response.isOk()) {
                log.warn("Error occurred {}, while trying to process '{}'",
                    response.description(), update.message().text()
                );
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    public void sendMessage(SendMessage sendMessage) throws TgChatBotException {
        SendResponse response = telegramBot.execute(sendMessage);
        if (!response.isOk()) {
            throw new TgChatBotException(
                response.description(),
                "Can't send message"
            );
        }
    }

    @Override
    @PostConstruct
    public void start() {
        telegramBot.execute(new SetMyCommands(chatProcessor.getBotCommands()));
        telegramBot.setUpdatesListener(this::handleUpdates);

        log.info("Bot was successfully started");
    }

    @Override
    public void close() {
        telegramBot.shutdown();

        log.info("Bot was successfully stopped");
    }
}

package edu.java.bot.service;

import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.base.Bot;
import edu.java.bot.exceptions.TgChatBotException;
import edu.java.dto.request.LinkUpdate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MarkdownV2NotificationService implements NotificationService {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss dd MMMM yyyy");

    private final Bot bot;

    private String prepareMessage(LinkUpdate update) {
        return "*[LINK UPDATE](%s)\n%s*\n`%s`"
            .formatted(
                update.url(),
                DATE_FORMATTER.format(update.date()),
                update.description()
            );
    }

    @Override
    public void sendNotifications(LinkUpdate update) throws TgChatBotException {
        String message = prepareMessage(update);
        for (Long tgChatId: update.tgChatIds()) {
            bot.sendMessage(
                new SendMessage(tgChatId, message).parseMode(ParseMode.MarkdownV2)
            );
        }
    }
}

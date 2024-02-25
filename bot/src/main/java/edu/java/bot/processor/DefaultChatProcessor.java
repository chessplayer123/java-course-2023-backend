package edu.java.bot.processor;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.exceptions.UserIsNotRegisteredException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultChatProcessor implements ChatProcessor {
    private final List<Command> commands;

    @Override
    public SendMessage process(Update update) {
        long userId = (update.message() == null ? update.callbackQuery().from().id() : update.message().chat().id());

        for (Command cmd : commands) {
            if (!cmd.supports(update)) {
                continue;
            }

            try {
                return cmd.handle(update);
            } catch (UserIsNotRegisteredException e) {
                return new SendMessage(userId, "You are not registered. Type /start to register");
            }
        }

        return new SendMessage(userId, "Unknown command. Type /help to see list of commands");
    }

    @Override
    public BotCommand[] getBotCommands() {
        return commands.stream()
            .map(Command::toApiCommand)
            .toArray(BotCommand[]::new);
    }
}

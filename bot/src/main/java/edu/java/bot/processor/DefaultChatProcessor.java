package edu.java.bot.processor;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import java.util.ArrayList;
import java.util.List;

public class DefaultChatProcessor implements ChatProcessor {
    private final List<Command> commands = new ArrayList<>();

    @Override
    public ChatProcessor addCommand(Command command) {
        commands.add(command);
        return this;
    }

    @Override
    public SendMessage process(Update update) {
        for (Command cmd : commands) {
            if (cmd.supports(update)) {
                return cmd.handle(update);
            }
        }
        return new SendMessage(update.message().chat().id(), "Unknown command. Type /help to see list of commands");
    }

    @Override
    public BotCommand[] getBotCommands() {
        return commands.stream()
            .map(Command::toApiCommand)
            .toArray(BotCommand[]::new);
    }
}

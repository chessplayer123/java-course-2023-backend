package edu.java.bot.processor;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import java.util.Arrays;
import java.util.stream.Collectors;

public interface ChatProcessor {
    SendMessage process(Update update);

    BotCommand[] getBotCommands();

    ChatProcessor addCommand(Command command);

    default ChatProcessor buildHelpCommand(String message) {
        BotCommand[] commands = getBotCommands();
        String commandsInfo = Arrays.stream(commands)
            .map(cmd -> "%s - %s".formatted(cmd.command(), cmd.description()))
            .collect(Collectors.joining("\n"));

        addCommand(new Command() {
            @Override
            public String command() {
                return "/help";
            }

            @Override
            public String description() {
                return "print help message";
            }

            @Override
            public SendMessage handle(Update update) {
                return new SendMessage(
                    update.message().chat().id(),
                    "%s\n%s".formatted(message, commandsInfo)
                );
            }
        });

        return this;
    }
}

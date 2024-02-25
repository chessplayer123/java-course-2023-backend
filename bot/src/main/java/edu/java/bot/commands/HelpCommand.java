package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {
    private final List<Command> commands;

    public HelpCommand(List<Command> commands) {
        this.commands = commands;
        this.commands.add(this);
    }

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
        return new SendMessage(update.message().chat().id(), getCommandsDescription());
    }

    private String getCommandsDescription() {
        return "List of supported commands:\n%s".formatted(
            commands.stream()
                .map(command -> "%s - %s".formatted(command.command(), command.description()))
                .collect(Collectors.joining("\n"))
        );
    }
}

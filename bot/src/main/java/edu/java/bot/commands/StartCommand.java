package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.exceptions.CommandException;
import edu.java.bot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StartCommand implements Command {
    private final UserService userService;

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "register user";
    }

    @Override
    public SendMessage handle(Update update) throws CommandException {
        userService.registerChat(update.message().chat().id());
        return new SendMessage(
            update.message().chat().id(),
            "%s, you are welcome. Type /help for commands".formatted(update.message().chat().firstName())
        );
    }
}

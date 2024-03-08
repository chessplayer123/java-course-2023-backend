package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.exceptions.CommandException;
import edu.java.bot.service.UserService;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TrackCommand implements Command {
    private final Set<Long> expectedUsers = new HashSet<>();
    private final UserService userService;

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "add new link to tracked list";
    }

    @Override
    public boolean supports(Update update) {
        if (update.message() == null) {
            return false;
        } else if (expectedUsers.contains(update.message().chat().id())) {
            return true;
        }
        return update.message().text().equals(command());
    }

    @Override
    public SendMessage handle(Update update) throws CommandException {
        long userId = update.message().chat().id();
        if (!expectedUsers.contains(userId)) {
            expectedUsers.add(userId);
            return new SendMessage(userId, "Enter the link:");
        }
        expectedUsers.remove(userId);
        userService.trackLink(userId, update.message().text());
        return new SendMessage(userId, "Link was successfully added to your track list");
    }
}

package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.link.DomainIsNotSupportedException;
import edu.java.bot.link.Link;
import edu.java.bot.link.LinkHandler;
import edu.java.bot.user.UserIsNotRegisteredException;
import edu.java.bot.user.UserService;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TrackCommand implements Command {
    private final UserService userService;
    private final LinkHandler linkHandler;
    private final Set<Long> expectedUsers = new HashSet<>();

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "add new tracked link";
    }

    @Override
    public boolean supports(Update update) {
        if (expectedUsers.contains(update.message().chat().id())) {
            return true;
        }
        return update.message().text().equals(command());
    }

    @Override
    public SendMessage handle(Update update) {
        long userId = update.message().chat().id();
        if (!expectedUsers.contains(userId)) {
            expectedUsers.add(userId);
            return new SendMessage(userId, "Enter the link:");
        }

        expectedUsers.remove(userId);
        String message = "Link was successfully added to your track list";
        try {
            String domain = linkHandler.getDomain(update.message().text());
            userService.trackLink(userId, new Link(update.message().text(), domain));
        } catch (UserIsNotRegisteredException e) {
            message = "You are not registered. Type /start to register";
        } catch (DomainIsNotSupportedException e) {
            message = "Domain is not supported.";
        }

        return new SendMessage(userId, message);
    }
}

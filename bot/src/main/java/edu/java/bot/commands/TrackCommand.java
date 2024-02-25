package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.exceptions.DomainIsNotSupportedException;
import edu.java.bot.exceptions.UserIsNotRegisteredException;
import edu.java.bot.link.Link;
import edu.java.bot.link.LinkHandlerChain;
import edu.java.bot.user.UserService;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TrackCommand implements Command {
    private final UserService userService;
    private final LinkHandlerChain handlerChain;
    private final Set<Long> expectedUsers = new HashSet<>();

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
    public SendMessage handle(Update update) throws UserIsNotRegisteredException {
        long userId = update.message().chat().id();
        if (!userService.isUserRegistered(userId)) {
            throw new UserIsNotRegisteredException();
        } else if (!expectedUsers.contains(userId)) {
            expectedUsers.add(userId);
            return new SendMessage(userId, "Enter the link:");
        }

        expectedUsers.remove(userId);
        String message = "Link was successfully added to your track list";
        try {
            String domain = handlerChain.getDomain(update.message().text());
            userService.trackLink(userId, new Link(update.message().text(), domain));
        } catch (DomainIsNotSupportedException e) {
            message = "Link is not supported for now";
        }

        return new SendMessage(userId, message);
    }
}

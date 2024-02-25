package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.exceptions.UserIsNotRegisteredException;
import edu.java.bot.link.Link;
import edu.java.bot.user.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ListCommand implements Command {
    private final UserService userService;

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "list all subscribed links";
    }

    @Override
    public SendMessage handle(Update update) throws UserIsNotRegisteredException {
        Set<Link> links = userService.getTrackedLinks(update.message().chat().id());
        if (links.isEmpty()) {
            return new SendMessage(update.message().chat().id(), "You aren't tracking any links");
        }

        StringBuilder builder = new StringBuilder();

        builder.append("List of tracked links:\n");
        int i = 0;
        for (Link link : links) {
            builder.append(++i)
                .append(". ")
                .append(link.url())
                .append(" [Resource: ")
                .append(link.domain())
                .append("]\n");
        }

        return new SendMessage(update.message().chat().id(), builder.toString());
    }
}

package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.exceptions.CommandException;
import edu.java.bot.service.UserService;
import edu.java.dto.response.LinkResponse;
import java.util.List;
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
    public SendMessage handle(Update update) throws CommandException {
        List<LinkResponse> links = userService.getTrackedLinks(update.message().chat().id());
        if (links.isEmpty()) {
            return new SendMessage(update.message().chat().id(), "You aren't tracking any links");
        }

        StringBuilder builder = new StringBuilder();

        builder.append("List of tracked links:\n");
        int i = 0;
        for (LinkResponse link : links) {
            builder.append("%d. %s [%s]\n".formatted(++i, link.description(), link.url()));
        }

        return new SendMessage(update.message().chat().id(), builder.toString());
    }
}

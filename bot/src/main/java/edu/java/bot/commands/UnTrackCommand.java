package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.link.Link;
import edu.java.bot.user.UserIsNotRegisteredException;
import edu.java.bot.user.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UnTrackCommand implements Command {
    private final UserService userService;

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "untrack link";
    }

    @Override
    public SendMessage handle(Update update) {
        try {
            boolean isUntracked = userService.unTrackLink(update.message().chat().id(), new Link("", ""));
            return new SendMessage(
                update.message().chat().id(),
                isUntracked ? "Links was successfully untracked" : "Link hasn't been tracked"
            );
        } catch (UserIsNotRegisteredException e) {
            return new SendMessage(update.message().chat().id(), "You are not registered");
        }
    }
}

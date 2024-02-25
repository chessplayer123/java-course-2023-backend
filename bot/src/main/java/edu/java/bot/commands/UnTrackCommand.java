package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.exceptions.UserIsNotRegisteredException;
import edu.java.bot.link.Link;
import edu.java.bot.user.UserService;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UnTrackCommand implements Command {
    private static final String URL_DOMAIN_SEPARATOR = ";;";

    private final UserService userService;
    private final Set<Long> expectedUsers = new HashSet<>();

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "untrack link";
    }

    @Override
    public boolean supports(Update update) {
        if (update.message() == null) {
            return expectedUsers.contains(update.callbackQuery().from().id());
        }
        return update.message().text().equals(command());
    }

    public SendMessage handleCallback(Update update) throws UserIsNotRegisteredException {
        long userId = update.callbackQuery().from().id();
        String[] linkDomain = update.callbackQuery().data().split(URL_DOMAIN_SEPARATOR);
        userService.unTrackLink(userId, new Link(linkDomain[0], linkDomain[1]));
        return new SendMessage(
            userId,
            "Link was successfully untracked"
        );
    }

    @Override
    public SendMessage handle(Update update) throws UserIsNotRegisteredException {
        if (update.message() == null) {
            return handleCallback(update);
        }

        Set<Link> links = userService.getTrackedLinks(update.message().chat().id());
        if (links.isEmpty()) {
            return new SendMessage(update.message().chat().id(), "Your tracking list is empty");
        }
        expectedUsers.add(update.message().chat().id());
        return new SendMessage(update.message().chat().id(), "Choose link to untrack")
            .replyMarkup(new InlineKeyboardMarkup(
                links.stream()
                .map(link -> new InlineKeyboardButton[] {
                    new InlineKeyboardButton(link.url()).callbackData(link.url() + URL_DOMAIN_SEPARATOR + link.domain())
                }).toArray(InlineKeyboardButton[][]::new)
            )
        );
    }
}

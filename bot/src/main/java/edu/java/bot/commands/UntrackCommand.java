package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.exceptions.CommandException;
import edu.java.bot.service.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UntrackCommand implements Command {
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

    public SendMessage handleCallback(Update update) throws CommandException {
        long userId = update.callbackQuery().from().id();
        String link = update.callbackQuery().data();
        userService.unTrackLink(userId, link);
        return new SendMessage(
            userId,
            "Link was successfully untracked"
        );
    }

    @Override
    public SendMessage handle(Update update) throws CommandException {
        if (update.message() == null) {
            return handleCallback(update);
        }

        List<String> links = userService.getTrackedLinks(update.message().chat().id());
        if (links.isEmpty()) {
            return new SendMessage(update.message().chat().id(), "Your tracking list is empty");
        }
        expectedUsers.add(update.message().chat().id());
        return new SendMessage(update.message().chat().id(), "Choose link to untrack")
            .replyMarkup(new InlineKeyboardMarkup(
                links.stream()
                .map(link -> new InlineKeyboardButton[] {
                    new InlineKeyboardButton(link).callbackData(link)
                }).toArray(InlineKeyboardButton[][]::new)
            )
        );
    }
}

package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.exceptions.CommandException;
import edu.java.bot.service.UserService;
import edu.java.dto.response.LinkResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UntrackCommand implements Command {
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
    public boolean supports(Update update) {
        return update.callbackQuery() != null || update.message().text().equals(command());
    }

    public SendMessage handleCallback(Update update) throws CommandException {
        long userId = update.callbackQuery().from().id();
        long linkId = Long.parseLong(update.callbackQuery().data());

        String url = userService.getTrackedLinks(userId)
            .stream()
            .filter(link -> link.id().equals(linkId))
            .findAny()
            .orElseThrow(() -> new CommandException("Link was already removed"))
            .url()
            .toString();

        userService.untrackLink(userId, url);

        return new SendMessage(userId, "Link was successfully untracked");
    }

    @Override
    public SendMessage handle(Update update) throws CommandException {
        if (update.callbackQuery() != null) {
            return handleCallback(update);
        }

        List<LinkResponse> links = userService.getTrackedLinks(update.message().chat().id());
        if (links.isEmpty()) {
            return new SendMessage(update.message().chat().id(), "Your tracking list is empty");
        }

        return new SendMessage(update.message().chat().id(), "Choose link to untrack")
            .replyMarkup(new InlineKeyboardMarkup(
                links.stream()
                .map(link -> new InlineKeyboardButton[] {
                    new InlineKeyboardButton(link.url().toString())
                        .callbackData(String.valueOf(link.id()))
                }).toArray(InlineKeyboardButton[][]::new)
            )
        );
    }
}

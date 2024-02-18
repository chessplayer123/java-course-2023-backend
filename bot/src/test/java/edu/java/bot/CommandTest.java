package edu.java.bot;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UnTrackCommand;
import edu.java.bot.link.GithubLinkHandler;
import edu.java.bot.link.Link;
import edu.java.bot.link.LinkHandler;
import edu.java.bot.user.InMemoryUserService;
import edu.java.bot.user.UserIsNotRegisteredException;
import edu.java.bot.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class CommandTest {
    public static Update mockUpdate(long userId, String text) {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);

        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn(text);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(userId);

        return update;
    }

    public static Arguments[] botCommands() {
        return new Arguments[] {
            Arguments.of(new ListCommand(null), new BotCommand("/list", "list all subscribed links")),
            Arguments.of(new StartCommand(null), new BotCommand("/start", "register user")),
            Arguments.of(new TrackCommand(null, null), new BotCommand("/track", "add new tracked link")),
            Arguments.of(new UnTrackCommand(null), new BotCommand("/untrack", "untrack link")),
        };
    }

    @Test
    public void listEmptyCommandsShouldSendSpecialMessage() throws UserIsNotRegisteredException {
        UserService userService = new InMemoryUserService();
        Command command = new ListCommand(userService);
        Update update = mockUpdate(0L, "/list");

        userService.registerUser(0);
        Map<String, Object> expectedMessage = Map.of(
            "chat_id", 0L,
            "text", "You aren't tracking any links"
        );
        Map<String, Object> actualMessage = command.handle(update).getParameters();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void listCommandShouldSendTrackedLinks() throws UserIsNotRegisteredException {
        UserService userService = new InMemoryUserService();
        Command command = new ListCommand(userService);
        Update update = mockUpdate(0L, "/list");

        userService.registerUser(0);
        userService.trackLink(0, new Link("Link 1", "Domain"));
        Map<String, Object> expectedMessage = Map.of(
            "chat_id", 0L,
            "text", "List of tracked links:\n1. Link 1 [Resource: Domain]\n"
        );
        Map<String, Object> actualMessage = command.handle(update).getParameters();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void trackCommandShouldAddLinkToInMemoryDatabase() throws UserIsNotRegisteredException {
        UserService userService = new InMemoryUserService();
        LinkHandler linkHandler = new GithubLinkHandler();
        Command command = new TrackCommand(userService, linkHandler);

        userService.registerUser(6L);
        Update trackUpdate = mockUpdate(6L, "/track");
        command.handle(trackUpdate);
        Update linkUpdate = mockUpdate(6L, "https://github.com");
        command.handle(linkUpdate);

        assertThat(userService.getTrackedLinks(6L))
            .containsExactly(new Link("https://github.com", "github.com"));
    }

    @Test
    public void startCommandShouldRegisterNewUser() throws UserIsNotRegisteredException {
        UserService userService = new InMemoryUserService();
        Command command = new StartCommand(userService);
        long userId = 10L;
        Update update = mockUpdate(userId, "/start");

        command.handle(update);

        assertThatCode(() -> userService.getTrackedLinks(userId))
            .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("botCommands")
    public void definedCommandsShouldReturnExpectedBotCommands(Command command, BotCommand expectedCommand) {
        BotCommand actualCommand = command.toApiCommand();

        assertThat(actualCommand).isEqualTo(expectedCommand);
    }

    @Test
    public void unTrackCommandShouldRemoveLinkFromTracked() throws UserIsNotRegisteredException {
        UserService userService = new InMemoryUserService();
        userService.registerUser(0L);
        userService.trackLink(0, new Link("Link", "Domain"));

        Command command = new UnTrackCommand(userService);

        Update untrackUpdate = mockUpdate(0L, "/untrack");
        command.handle(untrackUpdate);

        Update variantUpdate = Mockito.mock(Update.class);
        Mockito.when(variantUpdate.message()).thenReturn(null);
        CallbackQuery callback = Mockito.mock(CallbackQuery.class);
        User user = Mockito.mock(User.class);
        Mockito.when(variantUpdate.callbackQuery()).thenReturn(callback);
        Mockito.when(callback.data()).thenReturn("Link;;Domain");
        Mockito.when(callback.from()).thenReturn(user);
        Mockito.when(user.id()).thenReturn(0L);

        command.handle(variantUpdate);

        assertThat(userService.getTrackedLinks(0L)).isEmpty();
    }
}

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
import edu.java.bot.link.Link;
import edu.java.bot.exceptions.UserIsNotRegisteredException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommandTest extends AbstractTest {
    private long nextUserId = 0L;

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

    private Arguments[] nextUser() {
        return new Arguments[] {
            Arguments.of(nextUserId++)
        };
    }

    private Arguments[] botCommands() {
        return new Arguments[] {
            Arguments.of(new ListCommand(userService), new BotCommand("/list", "list all subscribed links")),
            Arguments.of(new StartCommand(userService), new BotCommand("/start", "register user")),
            Arguments.of(new TrackCommand(userService, linkProcessor), new BotCommand("/track", "add new link to tracked list")),
            Arguments.of(new UnTrackCommand(userService), new BotCommand("/untrack", "untrack link")),
        };
    }

    @ParameterizedTest
    @MethodSource("nextUser")
    public void listEmptyCommandsShouldSendSpecialMessage(long userId) throws UserIsNotRegisteredException {
        Command command = new ListCommand(userService);
        Update update = mockUpdate(userId, "/list");

        userService.registerUser(userId);
        Map<String, Object> expectedMessage = Map.of(
            "chat_id", userId,
            "text", "You aren't tracking any links"
        );
        Map<String, Object> actualMessage = command.handle(update).getParameters();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @ParameterizedTest
    @MethodSource("nextUser")
    public void listCommandShouldSendTrackedLinks(long userId) throws UserIsNotRegisteredException {
        Command command = new ListCommand(userService);
        Update update = mockUpdate(userId, "/list");

        userService.registerUser(userId);
        userService.trackLink(userId, new Link("Link 1", "Domain"));
        Map<String, Object> expectedMessage = Map.of(
            "chat_id", userId,
            "text", "List of tracked links:\n1. Link 1 [Resource: Domain]\n"
        );
        Map<String, Object> actualMessage = command.handle(update).getParameters();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @ParameterizedTest
    @MethodSource("nextUser")
    public void trackCommandShouldAddLinkToInMemoryDatabase(long userId) throws UserIsNotRegisteredException {
        Command command = new TrackCommand(userService, linkProcessor);

        userService.registerUser(userId);
        Update trackUpdate = mockUpdate(userId, "/track");
        command.handle(trackUpdate);
        Update linkUpdate = mockUpdate(userId, "https://github.com");
        command.handle(linkUpdate);

        assertThat(userService.getTrackedLinks(userId))
            .containsExactly(new Link("https://github.com", "github.com"));
    }

    @ParameterizedTest
    @MethodSource("nextUser")
    public void startCommandShouldRegisterNewUser(long userId) throws UserIsNotRegisteredException {
        Command command = new StartCommand(userService);
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

    @ParameterizedTest
    @MethodSource("nextUser")
    public void unTrackCommandShouldRemoveLinkFromTracked(long userId) throws UserIsNotRegisteredException {
        userService.registerUser(userId);
        userService.trackLink(userId, new Link("Link", "Domain"));

        Command command = new UnTrackCommand(userService);

        Update untrackUpdate = mockUpdate(userId, "/untrack");
        command.handle(untrackUpdate);

        Update variantUpdate = Mockito.mock(Update.class);
        Mockito.when(variantUpdate.message()).thenReturn(null);
        CallbackQuery callback = Mockito.mock(CallbackQuery.class);
        User user = Mockito.mock(User.class);
        Mockito.when(variantUpdate.callbackQuery()).thenReturn(callback);
        Mockito.when(callback.data()).thenReturn("Link;;Domain");
        Mockito.when(callback.from()).thenReturn(user);
        Mockito.when(user.id()).thenReturn(userId);

        command.handle(variantUpdate);

        assertThat(userService.getTrackedLinks(userId)).isEmpty();
    }
}

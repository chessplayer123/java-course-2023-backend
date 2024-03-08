package edu.java.bot;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UnTrackCommand;
import edu.java.bot.service.ScrapperUserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
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
            Arguments.of(new TrackCommand(userService), new BotCommand("/track", "add new link to tracked list")),
            Arguments.of(new UnTrackCommand(userService), new BotCommand("/untrack", "untrack link")),
        };
    }

    @ParameterizedTest
    @SneakyThrows
    @MethodSource("nextUser")
    public void listEmptyCommandsShouldSendSpecialMessage(long userId) {
        Command command = new ListCommand(userService);
        Update update = mockUpdate(userId, "/list");

        Mockito.when(userService.getTrackedLinks(userId))
                .thenReturn(Collections.emptyList());

        Map<String, Object> actualMessage = command.handle(update).getParameters();

        assertThat(actualMessage)
            .extracting("chat_id", "text")
            .containsExactly(userId, "You aren't tracking any links");
    }

    @ParameterizedTest
    @SneakyThrows
    @MethodSource("nextUser")
    public void listCommandShouldReturnTrackedLinks(long userId) {
        Command command = new ListCommand(userService);
        Update update = mockUpdate(userId, "/list");

        Mockito.when(userService.getTrackedLinks(userId))
            .thenReturn(List.of("Link 1", "Link 2"));

        Map<String, Object> actualMessage = command.handle(update).getParameters();

        assertThat(actualMessage)
            .extracting("chat_id", "text")
            .containsExactly(userId, "List of tracked links:\n1. Link 1\n2. Link 2\n");
    }

    @ParameterizedTest
    @SneakyThrows
    @MethodSource("nextUser")
    public void trackCommandShouldCallUserServiceMethod(long userId) {
        Command command = new TrackCommand(userService);
        Update trackUpdate = mockUpdate(userId, "/track");
        Update linkUpdate = mockUpdate(userId, "https://github.com");

        command.handle(trackUpdate);
        command.handle(linkUpdate);

        assertThatCode(() -> Mockito
            .verify(userService)
            .trackLink(userId, "https://github.com")
        ).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @SneakyThrows
    @MethodSource("nextUser")
    public void startCommandShouldRegisterNewUser(long userId) {
        Command command = new StartCommand(userService);
        Update update = mockUpdate(userId, "/start");

        command.handle(update);

        assertThatCode(() -> Mockito
            .verify(userService)
            .registerChat(userId)
        ).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("botCommands")
    public void definedCommandsShouldReturnExpectedBotCommands(Command command, BotCommand expectedCommand) {
        BotCommand actualCommand = command.toApiCommand();

        assertThat(actualCommand).isEqualTo(expectedCommand);
    }

    @ParameterizedTest
    @SneakyThrows
    @MethodSource("nextUser")
    public void unTrackCommandShouldRemoveLinkFromTracked(long userId) {
        Command command = new UnTrackCommand(userService);
        Update untrackUpdate = mockUpdate(userId, "/untrack");

        command.handle(untrackUpdate);

        Update variantUpdate = Mockito.mock(Update.class);
        Mockito.when(variantUpdate.message()).thenReturn(null);
        CallbackQuery callback = Mockito.mock(CallbackQuery.class);
        User user = Mockito.mock(User.class);
        Mockito.when(variantUpdate.callbackQuery()).thenReturn(callback);
        Mockito.when(callback.data()).thenReturn("Link");
        Mockito.when(callback.from()).thenReturn(user);
        Mockito.when(user.id()).thenReturn(userId);

        command.handle(variantUpdate);

        assertThatCode(() -> Mockito
            .verify(userService)
            .unTrackLink(userId, "Link")
        ).doesNotThrowAnyException();
    }
}

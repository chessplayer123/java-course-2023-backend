package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.link.GithubLinkHandler;
import edu.java.bot.link.Link;
import edu.java.bot.link.LinkHandler;
import edu.java.bot.user.InMemoryUserService;
import edu.java.bot.user.UserIsNotRegisteredException;
import edu.java.bot.user.UserService;
import org.junit.jupiter.api.Test;
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

    @Test
    public void listEmptyCommandsShouldSendSpecialMessage() {
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
    public void startCommandShouldRegisterNewUser() {
        UserService userService = new InMemoryUserService();
        Command command = new StartCommand(userService);
        long userId = 10L;
        Update update = mockUpdate(userId, "/start");

        command.handle(update);

        assertThatCode(() -> userService.getTrackedLinks(userId))
            .doesNotThrowAnyException();
    }
}

package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.link.Link;
import edu.java.bot.user.InMemoryUserService;
import edu.java.bot.user.UserIsNotRegisteredException;
import edu.java.bot.user.UserService;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

public class CommandTest {
    public static Update mockUpdate(long userId, String text) {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);

        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("/list");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(0L);

        return update;
    }

    @Test
    public void listCommandShouldSendTrackedLinks() throws UserIsNotRegisteredException {
        UserService userService = new InMemoryUserService();

        Command command = new ListCommand(userService);

        userService.registerUser(0);
        userService.trackLink(0, new Link("Link 1", "Domain"));

        Update update = mockUpdate(0L, "/list");

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

        Command command = new TrackCommand(userService);

        userService.registerUser(0);
        userService.trackLink(0, new Link("Link 1", "Domain"));

        Update update = mockUpdate(6L, "");

        Map<String, Object> expectedMessage = Map.of(
            "chat_id", 0L,
            "text", "List of tracked links:\n1. Link 1 [Resource: Domain]\n"
        );

        Map<String, Object> actualMessage = command.handle(update).getParameters();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }
}

package edu.java.bot;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UnTrackCommand;
import edu.java.bot.processor.ChatProcessor;
import edu.java.bot.processor.DefaultChatProcessor;
import edu.java.bot.user.InMemoryUserRepository;
import edu.java.bot.user.InMemoryUserService;
import edu.java.bot.user.UserService;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

public class ChatProcessorTest {
    @Test
    public void unknownCommandShouldReturnExpectedMessage() {
        UserService userService = new InMemoryUserService(new InMemoryUserRepository());
        ChatProcessor chatProcessor = new DefaultChatProcessor(List.of(
            new ListCommand(userService),
            new StartCommand(userService),
            new UnTrackCommand(userService)
        ));
        Update update = CommandTest.mockUpdate(1L, "/unknown");

        Map<String, Object> actualMessage = chatProcessor.process(update).getParameters();
        Map<String, Object> expectedMessage = Map.of(
            "chat_id", 1L,
            "text", "Unknown command. Type /help to see list of commands"
        );

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void getBotCommandsMethodShouldReturnAllAddedCommands() {
        ChatProcessor chatProcessor = new DefaultChatProcessor(List.of(
            new StartCommand(null),
            new UnTrackCommand(null)
        ));

        BotCommand[] actualBotCommands = chatProcessor.getBotCommands();
        BotCommand[] expectedBotCommands = new BotCommand[] {
            new BotCommand("/start", "register user"),
            new BotCommand("/untrack", "untrack link")
        };

        assertThat(actualBotCommands).containsOnly(expectedBotCommands);
    }
}

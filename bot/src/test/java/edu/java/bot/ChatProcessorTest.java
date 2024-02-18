package edu.java.bot;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.UnTrackCommand;
import edu.java.bot.processor.ChatProcessor;
import edu.java.bot.processor.DefaultChatProcessor;
import org.junit.Test;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

public class ChatProcessorTest {
    @Test
    public void unknownCommandShouldReturnExpectedMessage() {
        ChatProcessor chatProcessor = new DefaultChatProcessor();
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
        ChatProcessor chatProcessor = new DefaultChatProcessor()
            .addCommand(new StartCommand(null))
            .addCommand(new UnTrackCommand(null));

        BotCommand[] actualBotCommands = chatProcessor.getBotCommands();
        BotCommand[] expectedBotCommands = new BotCommand[] {
            new BotCommand("/start", "register user"),
            new BotCommand("/untrack", "untrack link")
        };

        assertThat(actualBotCommands).containsOnly(expectedBotCommands);
    }

    @Test
    public void autoBuiltHelpMessageShouldContainsExpectedData() {
        ChatProcessor chatProcessor = new DefaultChatProcessor()
            .addCommand(new StartCommand(null))
            .addCommand(new UnTrackCommand(null))
            .buildHelpCommand("my message");
        Update update = CommandTest.mockUpdate(0L, "/help");

        Map<String, Object> actualMessage = chatProcessor.process(update).getParameters();
        Map<String, Object> expectedMessage = Map.of(
            "chat_id", 0L,
            "text", "my message\n/start - register user\n/untrack - untrack link"
        );

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }
}

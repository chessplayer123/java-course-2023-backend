package edu.java.bot;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.processor.ChatProcessor;
import edu.java.bot.processor.DefaultChatProcessor;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

public class ChatProcessorTest {
    @Test
    public void unknownCommandShouldReturnExpectedMessage() {
        ChatProcessor chatProcessor = new DefaultChatProcessor(List.of(
            new ListCommand(null),
            new StartCommand(null),
            new UntrackCommand(null)
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
            new UntrackCommand(null)
        ));

        BotCommand[] actualBotCommands = chatProcessor.getBotCommands();
        BotCommand[] expectedBotCommands = new BotCommand[] {
            new BotCommand("/start", "register user"),
            new BotCommand("/untrack", "untrack link")
        };

        assertThat(actualBotCommands).containsOnly(expectedBotCommands);
    }
}

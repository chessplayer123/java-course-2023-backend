package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.exceptions.CommandException;

public interface Command {
    String command();

    String description();

    default boolean supports(Update update) {
        return update.message() != null && update.message().text().equals(command());
    }

    SendMessage handle(Update update) throws CommandException;

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}

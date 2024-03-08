package edu.java.bot.exceptions;

import lombok.Getter;

@Getter
public class CommandException extends Exception {
    public CommandException(String message) {
        super(message);
    }
}

package edu.java.bot.exceptions;

import lombok.Getter;

@Getter
public class CommandException extends RuntimeException {
    public CommandException(String message) {
        super(message);
    }
}

package edu.java.bot.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TgChatBotException extends Exception {
    private final String description;
    private final String errorCode;
    private final String errorMessage;
}

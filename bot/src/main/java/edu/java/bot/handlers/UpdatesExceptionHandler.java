package edu.java.bot.handlers;

import edu.java.bot.exceptions.TgChatBotException;
import edu.java.dto.response.ApiErrorResponse;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class UpdatesExceptionHandler {
    @ExceptionHandler(TgChatBotException.class)
    public ApiErrorResponse tgChatBotException(TgChatBotException e) {
        return new ApiErrorResponse(
            e.getDescription(),
            e.getErrorCode(),
            "TgChatBotException",
            e.getErrorMessage(),
            Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .toList()
        );
    }
}

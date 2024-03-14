package edu.java.bot.handlers;

import edu.java.bot.exceptions.TgChatBotException;
import edu.java.dto.response.ApiErrorResponse;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class UpdatesExceptionHandler {
    private <E extends Exception> List<String> getStackStrace(E exception) {
        return Arrays.stream(exception.getStackTrace())
            .map(StackTraceElement::toString)
            .toList();
    }

    @ExceptionHandler(TgChatBotException.class)
    public ApiErrorResponse tgChatBotException(TgChatBotException e) {
        return new ApiErrorResponse(
            e.getDescription(),
            e.getErrorCode(),
            e.getClass().getName(),
            e.getErrorMessage(),
            getStackStrace(e)
        );
    }
}

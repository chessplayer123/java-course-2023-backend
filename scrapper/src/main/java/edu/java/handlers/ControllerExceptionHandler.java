package edu.java.handlers;

import edu.java.dto.response.ApiErrorResponse;
import edu.java.exceptions.InvalidLinkException;
import edu.java.exceptions.LinkIsNotPresentException;
import edu.java.exceptions.LinkIsNotSupportedException;
import edu.java.exceptions.ReAddingLinkException;
import edu.java.exceptions.ReAddingUserException;
import edu.java.exceptions.UserIsNotRegisteredException;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@SuppressWarnings("MultipleStringLiterals")
public class ControllerExceptionHandler {
    private <E extends Exception> List<String> getStackStrace(E exception) {
        return Arrays.stream(exception.getStackTrace())
            .map(StackTraceElement::toString)
            .toList();
    }

    @ExceptionHandler(UserIsNotRegisteredException.class)
    public ApiErrorResponse userIsNotRegistered(UserIsNotRegisteredException exception) {
        return new ApiErrorResponse(
            exception.getDescription(),
            "401",
            exception.getClass().getName(),
            exception.getMessage(),
            getStackStrace(exception)
        );
    }

    @ExceptionHandler(ReAddingLinkException.class)
    public ApiErrorResponse linkReAdding(ReAddingLinkException exception) {
        return new ApiErrorResponse(
            exception.getDescription(),
            "409",
            exception.getClass().getName(),
            exception.getMessage(),
            getStackStrace(exception)
        );
    }

    @ExceptionHandler(ReAddingUserException.class)
    public ApiErrorResponse userReAdding(ReAddingUserException exception) {
        return new ApiErrorResponse(
            exception.getDescription(),
            "409",
            exception.getClass().getName(),
            exception.getMessage(),
            getStackStrace(exception)
        );
    }

    @ExceptionHandler(LinkIsNotPresentException.class)
    public ApiErrorResponse linkIsNotPresent(LinkIsNotPresentException exception) {
        return new ApiErrorResponse(
            exception.getDescription(),
            "404",
            exception.getClass().getName(),
            exception.getMessage(),
            getStackStrace(exception)
        );
    }

    @ExceptionHandler(LinkIsNotSupportedException.class)
    public ApiErrorResponse linkIsNotSupported(LinkIsNotSupportedException exception) {
        return new ApiErrorResponse(
            exception.getDescription(),
            "400",
            exception.getClass().getName(),
            exception.getMessage(),
            getStackStrace(exception)
        );
    }

    @ExceptionHandler(InvalidLinkException.class)
    public ApiErrorResponse linkIsNotValid(InvalidLinkException exception) {
        return new ApiErrorResponse(
            exception.getDescription(),
            "400",
            exception.getClass().getName(),
            exception.getMessage(),
            getStackStrace(exception)
        );
    }
}

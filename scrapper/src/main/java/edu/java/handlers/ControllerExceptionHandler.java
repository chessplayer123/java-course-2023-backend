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
            "You are not registered",
            "401",
            "UserIsNotRegisteredException",
            "User is not registered",
            getStackStrace(exception)
        );
    }

    @ExceptionHandler(ReAddingLinkException.class)
    public ApiErrorResponse linkReAdding(ReAddingLinkException exception) {
        return new ApiErrorResponse(
            "Link is already in your track list",
            "409",
            "ReAddingLinkException",
            "Attempting to re-add link to same user",
            getStackStrace(exception)
        );
    }

    @ExceptionHandler(ReAddingUserException.class)
    public ApiErrorResponse userReAdding(ReAddingUserException exception) {
        return new ApiErrorResponse(
            "You are already registered",
            "409",
            "ReAddingUserException",
            "Attempting to re-add already registered user",
            getStackStrace(exception)
        );
    }

    @ExceptionHandler(LinkIsNotPresentException.class)
    public ApiErrorResponse linkIsNotPresent(LinkIsNotPresentException exception) {
        return new ApiErrorResponse(
            "Link is not in your track list",
            "404",
            "LinkIsNotPresentException",
            "Attempting to delete not present link",
            getStackStrace(exception)
        );
    }

    @ExceptionHandler(LinkIsNotSupportedException.class)
    public ApiErrorResponse linkIsNotSupported(LinkIsNotSupportedException exception) {
        return new ApiErrorResponse(
            "Link is not supported",
            "400",
            "LinkIsNotSupportedException",
            "Attempting to track unsupported link",
            getStackStrace(exception)
        );
    }

    @ExceptionHandler(InvalidLinkException.class)
    public ApiErrorResponse linkIsNotValid(InvalidLinkException exception) {
        return new ApiErrorResponse(
            "Link is not valid",
            "400",
            "InvalidLinkException",
            "Invalid link",
            getStackStrace(exception)
        );
    }
}

package edu.java.handlers;

import edu.java.dto.response.ApiErrorResponse;
import edu.java.exceptions.ChatIsNotRegisteredException;
import edu.java.exceptions.InvalidLinkException;
import edu.java.exceptions.LinkIsNotPresentException;
import edu.java.exceptions.LinkIsNotSupportedException;
import edu.java.exceptions.ReAddingLinkException;
import edu.java.exceptions.ReAddingUserException;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
@SuppressWarnings("MultipleStringLiterals")
public class ControllerExceptionHandler {
    private <E extends Exception> List<String> getStackStrace(E exception) {
        return Arrays.stream(exception.getStackTrace())
            .map(StackTraceElement::toString)
            .toList();
    }

    private <T extends ResponseStatusException> ResponseEntity<ApiErrorResponse> wrapException(T exception) {
        return ResponseEntity
            .status(exception.getStatusCode())
            .body(new ApiErrorResponse(
                exception.getReason(),
                String.valueOf(exception.getStatusCode().value()),
                exception.getClass().toString(),
                exception.getStatusCode().toString(),
                getStackStrace(exception)
            ));
    }

    @ExceptionHandler(ChatIsNotRegisteredException.class)
    public ResponseEntity<ApiErrorResponse> userIsNotRegistered(ChatIsNotRegisteredException exception) {
        return wrapException(exception);
    }

    @ExceptionHandler(ReAddingLinkException.class)
    public ResponseEntity<ApiErrorResponse> linkReAdding(ReAddingLinkException exception) {
        return wrapException(exception);
    }

    @ExceptionHandler(ReAddingUserException.class)
    public ResponseEntity<ApiErrorResponse> userReAdding(ReAddingUserException exception) {
        return wrapException(exception);
    }

    @ExceptionHandler(LinkIsNotPresentException.class)
    public ResponseEntity<ApiErrorResponse> linkIsNotPresent(LinkIsNotPresentException exception) {
        return wrapException(exception);
    }

    @ExceptionHandler(LinkIsNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> linkIsNotSupported(LinkIsNotSupportedException exception) {
        return wrapException(exception);
    }

    @ExceptionHandler(InvalidLinkException.class)
    public ResponseEntity<ApiErrorResponse> linkIsNotValid(InvalidLinkException exception) {
        return wrapException(exception);
    }
}

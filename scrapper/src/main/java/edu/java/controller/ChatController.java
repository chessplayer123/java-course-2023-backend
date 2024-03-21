package edu.java.controller;

import edu.java.dto.response.ApiErrorResponse;
import edu.java.exceptions.ChatIsNotRegisteredException;
import edu.java.exceptions.ReAddingChatException;
import edu.java.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static edu.java.controller.Mappings.TG_CHAT_MAPPING;

@RestController
@RequiredArgsConstructor
@RequestMapping(TG_CHAT_MAPPING)
public class ChatController {
    private final ChatService service;

    @Operation(summary = "Delete chat with provided id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User was successfully deleted"),
        @ApiResponse(responseCode = "401", description = "User is not registered", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public void unregisterChat(@PathVariable Long id) throws ChatIsNotRegisteredException {
        service.unregister(id);
    }

    @Operation(summary = "Register new chat with provided id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User was successfully registered"),
        @ApiResponse(responseCode = "409", description = "User already registered", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/{id}")
    public void registerChat(@PathVariable Long id) throws ReAddingChatException {
        service.register(id);
    }
}

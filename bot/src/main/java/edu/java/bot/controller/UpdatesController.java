package edu.java.bot.controller;

import edu.java.bot.base.Bot;
import edu.java.bot.exceptions.TgChatBotException;
import edu.java.bot.service.UserService;
import edu.java.dto.request.LinkUpdate;
import edu.java.dto.response.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/updates")
public class UpdatesController {
    private final UserService service;
    private final Bot bot;

    @Operation(summary = "Process notification about tracked link update")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Update was successfully processed"),
        @ApiResponse(responseCode = "400", description = "Incorrect query", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public void handleLinkUpdate(@RequestBody LinkUpdate update) throws TgChatBotException {
        bot.sendNotifications(update.tgChatIds(), update.description());

        log.info("Notifications were successfully processed");
    }
}
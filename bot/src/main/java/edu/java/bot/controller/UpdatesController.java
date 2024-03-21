package edu.java.bot.controller;

import edu.java.bot.exceptions.TgChatBotException;
import edu.java.bot.service.NotificationService;
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
import static edu.java.bot.controller.Mappings.UPDATES_MAPPING;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping(UPDATES_MAPPING)
public class UpdatesController {
    private final NotificationService notificationService;

    @Operation(summary = "Process notification about tracked link update")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Update was successfully processed"),
        @ApiResponse(responseCode = "400", description = "Incorrect query", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public void handleLinkUpdate(@RequestBody LinkUpdate update) throws TgChatBotException {
        log.info("New notification received");

        notificationService.sendNotifications(update);

        log.info("Notifications [{}] were successfully sent", update.tgChatIds().size());
    }
}

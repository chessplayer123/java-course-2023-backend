package edu.java.bot.controller;

import edu.java.dto.request.LinkUpdate;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
public class UpdatesController {
    @Operation(summary = "Send notification about tracked link update")
    @PostMapping
    public void handleLinkUpdate(@RequestBody LinkUpdate update) {
    }
}

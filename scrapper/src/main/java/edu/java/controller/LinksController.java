package edu.java.controller;

import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.request.TrackLinkRequest;
import edu.java.dto.response.LinkResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
public class LinksController {
    @Operation(summary = "Remove link from tracking list")
    @DeleteMapping
    public LinkResponse unTrackLink(Long chatId, RemoveLinkRequest request) {
        return null;
    }

    @Operation(summary = "Add link to tracking list")
    @PostMapping
    public LinkResponse trackLink(Long chatId, TrackLinkRequest request) {
        return null;
    }

    @Operation(summary = "Get all user's tracked links")
    @GetMapping
    public LinkResponse getTrackedLinks(Long chatId) {
        return null;
    }
}

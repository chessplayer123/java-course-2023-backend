package edu.java.controller;

import edu.java.client.api.ApiClient;
import edu.java.dto.request.TrackLinkRequest;
import edu.java.dto.request.UntrackLinkRequest;
import edu.java.dto.response.ApiErrorResponse;
import edu.java.dto.response.LinkResponse;
import edu.java.dto.response.ListLinkResponse;
import edu.java.processor.LinkProcessor;
import edu.java.response.LinkApiResponse;
import edu.java.service.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static edu.java.controller.Mappings.LINKS_MAPPING;

@RestController
@RequiredArgsConstructor
@RequestMapping(LINKS_MAPPING)
public class LinksController {
    private final LinkProcessor processor;
    private final LinkService service;

    @Operation(summary = "Remove link from tracking list")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Link was successfully removed", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = LinkResponse.class)
        )),
        @ApiResponse(responseCode = "401", description = "User is not registered", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ApiErrorResponse.class)
        )),
        @ApiResponse(responseCode = "404", description = "Link is not tracked", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ApiErrorResponse.class)
        ))
    })
    @SneakyThrows
    @DeleteMapping
    public LinkResponse unTrackLink(
        @RequestHeader(value = "Tg-Chat-Id")
        Long chatId,
        @RequestBody
        UntrackLinkRequest request
    ) {
        URI url = URI.create(request.link());
        Long trackedLinkId = service.untrack(chatId, url);
        return new LinkResponse(trackedLinkId, url, null);
    }

    @Operation(summary = "Add link to tracking list")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Link was successfully added", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = LinkResponse.class)
        )),
        @ApiResponse(responseCode = "401", description = "User is not registered", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ApiErrorResponse.class)
        )),
        @ApiResponse(responseCode = "409", description = "Link already tracked", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ApiErrorResponse.class)
        ))
    })
    @SneakyThrows
    @PostMapping
    public LinkResponse trackLink(
        @RequestHeader(value = "Tg-Chat-Id")
        Long chatId,
        @RequestBody
        TrackLinkRequest request
    ) {
        URI link = URI.create(request.link());
        ApiClient client = processor.findClient(link);
        LinkApiResponse response = client.fetch(link);
        String description = response.getSummary();
        Long addedLinkId = service.track(chatId, link, description);
        return new LinkResponse(addedLinkId, link, description);
    }

    @Operation(summary = "Get all user's tracked links")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of links was successfully obtained", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ListLinkResponse.class)
        )),
        @ApiResponse(responseCode = "401", description = "User is not registered", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ApiErrorResponse.class)
        ))
    })
    @SneakyThrows
    @GetMapping
    public ListLinkResponse getTrackedLinks(
        @RequestHeader(value = "Tg-Chat-Id")
        Long chatId
    ) {
        List<LinkResponse> links = service
            .listAll(chatId)
            .stream()
            .map(link -> new LinkResponse(link.id(), link.url(), link.description()))
            .toList();
        return new ListLinkResponse(links, links.size());
    }
}

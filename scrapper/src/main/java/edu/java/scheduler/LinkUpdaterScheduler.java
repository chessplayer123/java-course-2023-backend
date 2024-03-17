package edu.java.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.client.api.ApiClient;
import edu.java.client.updates.UpdatesClient;
import edu.java.dto.request.LinkUpdate;
import edu.java.exceptions.InvalidLinkException;
import edu.java.exceptions.LinkIsNotSupportedException;
import edu.java.link.LinkProcessor;
import edu.java.repository.dto.Chat;
import edu.java.repository.dto.Link;
import edu.java.response.LinkApiResponse;
import edu.java.service.ChatService;
import edu.java.service.LinkService;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Log4j2
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final UpdatesClient updatesClient;
    private final LinkService linkService;
    private final ChatService chatService;
    private final LinkProcessor linkProcessor;

    @Scheduled(fixedDelayString = "#{@schedulerIntervalMs}")
    void update() {
        log.info("Starting update");
        for (Link link : linkService.getLinksCheckTimeExceedLimit(Duration.ofMinutes(1))) {
            try {
                ApiClient client = linkProcessor.findClient(link.url());
                LinkApiResponse updatedResponse = client.fetch(link.url());
                LinkApiResponse prevResponse = updatedResponse.deserializeFromJson(link.data());
                String events = updatedResponse.retrieveEvents(prevResponse);
                if (events != null) {
                    List<Long> subscribers = chatService.findChatsTrackingLink(link.id())
                        .stream()
                        .map(Chat::id)
                        .toList();
                    updatesClient.sendLinkUpdate(new LinkUpdate(link.id(), link.url(), events, subscribers));
                }
            } catch (LinkIsNotSupportedException | InvalidLinkException | JsonProcessingException ignored) {
            }
        }
        log.info("Update ended");
    }
}

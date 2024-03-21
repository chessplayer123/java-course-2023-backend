package edu.java.scheduler;

import edu.java.client.api.ApiClient;
import edu.java.client.api.LinkUpdateEvent;
import edu.java.client.updates.UpdatesClient;
import edu.java.dto.request.LinkUpdate;
import edu.java.processor.LinkProcessor;
import edu.java.repository.dto.Chat;
import edu.java.repository.dto.Link;
import edu.java.service.ChatService;
import edu.java.service.LinkService;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "app", name = "scheduler.enable", havingValue = "true")
@EnableScheduling
@Log4j2
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final UpdatesClient updatesClient;
    private final LinkService linkService;
    private final ChatService chatService;
    private final LinkProcessor linkProcessor;
    private final Duration forceCheckDelay;

    private String formatUpdate(LinkUpdateEvent update) {
        return "Link %s was updated at %s:\n%s".formatted(
            update.link(),
            update.date(),
            update.description()
        );
    }

    @SneakyThrows
    private void updateLink(Link link) {
        ApiClient client = linkProcessor.findClient(link.url());
        List<LinkUpdateEvent> updates = client.retrieveUpdates(link.url(), link.lastCheckTime());
        linkService.updateNow(link.id());

        if (updates.isEmpty()) {
            return;
        }

        List<Long> subscribers = chatService.findChatsTrackingLink(link.id())
            .stream()
            .map(Chat::id)
            .toList();

        String message = updates.stream()
            .map(this::formatUpdate)
            .collect(Collectors.joining("\n"));

        updatesClient.sendLinkUpdate(new LinkUpdate(
            link.id(),
            link.url(),
            message,
            subscribers
        ));
    }

    @Scheduled(fixedDelayString = "#{@schedulerIntervalMs}")
    public void update() {
        log.info("Starting update");
        for (Link link : linkService.getLinksCheckTimeExceedLimit(forceCheckDelay)) {
            updateLink(link);
        }
        log.info("Update ended");
    }
}

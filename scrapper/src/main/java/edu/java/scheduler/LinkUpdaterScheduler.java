package edu.java.scheduler;

import edu.java.client.updates.UpdatesClient;
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
    private final UpdatesClient client;

    @Scheduled(fixedDelayString = "#{@schedulerIntervalMs}")
    void update() {
        log.info("Update method called");
    }
}

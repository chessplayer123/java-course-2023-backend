package edu.java.scrapper.service.jpa;

import edu.java.repository.dto.Link;
import edu.java.repository.jpa.JpaChatRepository;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.repository.jpa.entity.ChatEntity;
import edu.java.repository.jpa.entity.LinkEntity;
import edu.java.scrapper.database.IntegrationEnvironment;
import edu.java.service.jpa.JpaLinkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import static org.assertj.core.api.Assertions.assertThat;

public class JpaLinkServiceTest extends IntegrationEnvironment {
    @Autowired
    private JpaLinkService linkService;
    @Autowired
    private JpaChatRepository chatRepository;
    @Autowired
    private JpaLinkRepository linkRepository;

    @Test
    @Transactional
    @Rollback
    public void trackShouldCreateSubscription() {
        Long chatId = 0L;
        ChatEntity addedChatEntity = chatRepository.save(new ChatEntity(chatId));

        Long addedLinkId = linkService.track(chatId, URI.create("https://github.com"), "description");

        LinkEntity addedLinkEntity = linkRepository.findById(addedLinkId).get();

        assertThat(addedLinkEntity.getSubscribedChats()).containsExactly(addedChatEntity);
        assertThat(addedChatEntity.getTrackedLinks()).containsExactly(addedLinkEntity);
    }

    @Test
    @Transactional
    @Rollback
    public void untrackShouldRemoveSubscription() {
        ChatEntity chatEntity = chatRepository.save(new ChatEntity(0L));
        LinkEntity linkEntity = linkRepository.save(
            new LinkEntity("https://github.com", "description", OffsetDateTime.now(), OffsetDateTime.now())
        );
        chatEntity.addLink(linkEntity);

        linkService.untrack(chatEntity.getId(), URI.create(linkEntity.getUrl()));

        assertThat(chatEntity.getTrackedLinks()).isEmpty();
        assertThat(linkRepository.findByUrl(linkEntity.getUrl()))
            .isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    public void listAllShouldReturnAllLinksTrackedByChat() {
        ChatEntity chatEntity = chatRepository.save(new ChatEntity(0L));
        LinkEntity linkEntity1 = linkRepository.save(
            new LinkEntity("https://github.com", "description", OffsetDateTime.now(), OffsetDateTime.now())
        );
        LinkEntity linkEntity2 = linkRepository.save(
            new LinkEntity("https://stackoverflow.com", "description", OffsetDateTime.now(), OffsetDateTime.now())
        );

        chatEntity.addLink(linkEntity1);
        chatEntity.addLink(linkEntity2);

        Collection<Link> actualLinks = linkService.listAll(chatEntity.getId());

        assertThat(actualLinks)
            .containsExactlyInAnyOrder(linkEntity1.toDto(), linkEntity2.toDto());
    }

    @Test
    @Transactional
    @Rollback
    public void updateShouldChangeEntity() {
        LinkEntity linkEntity = linkRepository.save(
            new LinkEntity("https://github.com", "description", OffsetDateTime.now(), OffsetDateTime.now())
        );

        OffsetDateTime updatedDate = OffsetDateTime.now().plus(Duration.ofDays(10));
        linkService.update(linkEntity.getId(), updatedDate);

        LinkEntity updatedEntity = linkRepository.findById(linkEntity.getId()).get();

        assertThat(updatedEntity.getLastCheckTime())
            .isEqualToIgnoringSeconds(updatedDate);
    }
}

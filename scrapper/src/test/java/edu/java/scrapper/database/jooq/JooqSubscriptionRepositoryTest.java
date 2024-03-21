package edu.java.scrapper.database.jooq;

import edu.java.repository.dto.Chat;
import edu.java.repository.dto.Link;
import edu.java.repository.dto.Subscription;
import edu.java.repository.jooq.JooqSubscriptionRepository;
import edu.java.scrapper.database.IntegrationEnvironment;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import static edu.java.domain.jooq.Tables.CHAT;
import static edu.java.domain.jooq.Tables.LINK;
import static edu.java.domain.jooq.Tables.SUBSCRIPTION;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {"app.scheduler.enable=false"})
public class JooqSubscriptionRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JooqSubscriptionRepository subscriptionRepository;
    @Autowired
    private DSLContext dslContext;

    @Test
    @Transactional
    @Rollback
    void addSubscriptionShouldInsertRecord() {
        Long chatId = 10L;
        dslContext.insertInto(CHAT, CHAT.ID)
            .values(chatId)
            .execute();
        Long linkId = dslContext.insertInto(LINK, LINK.URL, LINK.DESCRIPTION)
            .values("https://github.com", "Github")
            .returningResult(LINK.ID)
            .fetchOneInto(Long.class);

        subscriptionRepository.add(chatId, linkId);

        boolean isSubscriptionAdded = dslContext.selectFrom(SUBSCRIPTION)
            .where(SUBSCRIPTION.LINK_ID.eq(linkId))
            .and(SUBSCRIPTION.CHAT_ID.eq(chatId))
            .fetchOptional()
            .isPresent();

        assertThat(isSubscriptionAdded).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    void removeSubscriptionShouldDeleteRecord() {
        Long chatId = 10L;
        dslContext.insertInto(CHAT, CHAT.ID)
            .values(chatId)
            .execute();
        Long linkId = dslContext.insertInto(LINK, LINK.URL, LINK.DESCRIPTION)
            .values("https://github.com", "Github")
            .returningResult(LINK.ID)
            .fetchOneInto(Long.class);
        dslContext.insertInto(SUBSCRIPTION)
            .values(chatId, linkId)
            .execute();

        subscriptionRepository.remove(chatId, linkId);

        boolean isSubscriptionPresent = dslContext.selectFrom(SUBSCRIPTION)
            .where(SUBSCRIPTION.LINK_ID.eq(linkId))
            .and(SUBSCRIPTION.CHAT_ID.eq(chatId))
            .fetchOptional()
            .isPresent();

        assertThat(isSubscriptionPresent).isFalse();
    }

    @Test
    @Transactional
    @Rollback
    void findSubscriptionShouldReturnExpectedSubscription() {
        Long chatId = 111L;
        dslContext.insertInto(CHAT, CHAT.ID)
            .values(chatId)
            .execute();
        Long linkId = dslContext.insertInto(LINK, LINK.URL, LINK.DESCRIPTION)
            .values("https://github.com", "Github")
            .returningResult(LINK.ID)
            .fetchOneInto(Long.class);
        dslContext.insertInto(SUBSCRIPTION)
            .values(chatId, linkId)
            .execute();

        Optional<Subscription> foundSubscription = subscriptionRepository.findSubscription(chatId, linkId);

        assertThat(foundSubscription)
            .isPresent()
            .get()
            .extracting(Subscription::chatId, Subscription::linkId)
            .containsExactly(chatId, linkId);
    }

    @Test
    @Transactional
    @Rollback
    void findByLinkIdShouldReturnAllChatsTrackingLink() {
        Long chatId = 500L;
        dslContext.insertInto(CHAT, CHAT.ID)
            .values(chatId)
            .execute();
        dslContext.insertInto(CHAT, CHAT.ID)
            .values(0L)
            .execute();

        Long linkId = dslContext.insertInto(LINK, LINK.URL, LINK.DESCRIPTION)
            .values("https://github.com", "Github")
            .returningResult(LINK.ID)
            .fetchOneInto(Long.class);
        dslContext.insertInto(SUBSCRIPTION)
            .values(chatId, linkId)
            .execute();

        List<Long> subscribedChatsIds = subscriptionRepository
            .findByLinkId(linkId)
            .stream()
            .map(Chat::id)
            .toList();

        assertThat(subscribedChatsIds).containsExactly(chatId);
    }

    @Test
    @Transactional
    @Rollback
    void findByChatIdShouldReturnAllChatsTrackingLink() {
        Long chatId = 500L;
        dslContext.insertInto(CHAT, CHAT.ID)
            .values(chatId)
            .execute();

        Long linkId1 = dslContext.insertInto(LINK, LINK.URL, LINK.DESCRIPTION)
            .values("https://github.com", "Github")
            .returningResult(LINK.ID)
            .fetchOneInto(Long.class);
        Long linkId2 = dslContext.insertInto(LINK, LINK.URL, LINK.DESCRIPTION)
            .values("https://stackoverflow.com", "Github")
            .returningResult(LINK.ID)
            .fetchOneInto(Long.class);

        dslContext.insertInto(SUBSCRIPTION)
            .values(chatId, linkId1)
            .values(chatId, linkId2)
            .execute();

        List<Long> subscribedLinksIds = subscriptionRepository
            .findByChatId(chatId)
            .stream()
            .map(Link::id)
            .toList();

        assertThat(subscribedLinksIds).containsExactly(linkId1, linkId2);
    }
}

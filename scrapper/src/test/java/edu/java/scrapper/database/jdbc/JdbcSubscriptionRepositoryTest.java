package edu.java.scrapper.database.jdbc;

import edu.java.repository.dto.Chat;
import edu.java.repository.dto.Link;
import edu.java.repository.dto.Subscription;
import edu.java.repository.jdbc.JdbcSubscriptionRepository;
import edu.java.scrapper.database.IntegrationEnvironment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JdbcSubscriptionRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JdbcSubscriptionRepository subscriptionRepository;
    @Autowired
    private JdbcClient client;

    private Long registerChat(Long chatId) {
        client.sql("INSERT INTO chat(id) VALUES(?);")
            .params(chatId)
            .update();
        return chatId;
    }

    private Long nextLink(String url) {
        Link link = Link.from(URI.create(url), "Description", OffsetDateTime.now(), OffsetDateTime.now());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        client.sql("INSERT INTO link(url, description, created_at, last_check_time) VALUES(?, ?, ?, ?);")
            .params(link.url().toString(), link.description(), link.createdAt(), link.lastCheckTime())
            .update(keyHolder, "id");

        return keyHolder.getKeyAs(Long.class);
    }

    @Test
    @Transactional
    @Rollback
    void addSubscriptionShouldInsertRecord() {
        Long chatId = registerChat(1L);
        Long linkId = nextLink("https://github.com");

        subscriptionRepository.add(chatId, linkId);

        boolean isSubscriptionPresent = client.sql("SELECT * FROM subscription WHERE chat_id = ? and link_id = ?;")
            .params(chatId, linkId)
            .query(Subscription.class)
            .optional()
            .isPresent();

        assertThat(isSubscriptionPresent).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    void removeSubscriptionShouldDeleteRecord() {
        Long chatId = registerChat(1L);
        Long linkId = nextLink("https://github.com");

        client.sql("INSERT INTO subscription VALUES(?, ?);")
            .params(chatId, linkId)
            .update();

        subscriptionRepository.remove(chatId, linkId);
        boolean isSubscriptionPresent = client.sql("SELECT * FROM subscription WHERE chat_id = ? and link_id = ?;")
            .params(chatId, linkId)
            .query(Subscription.class)
            .optional()
            .isPresent();
        assertThat(isSubscriptionPresent).isFalse();
    }

    @Test
    @Transactional
    @Rollback
    void findSubscriptionShouldReturnExpectedSubscription() {
        Long chatId = registerChat(1L);
        Long linkId = nextLink("https://github.com");

        client.sql("INSERT INTO subscription VALUES(?, ?);")
            .params(chatId, linkId)
            .update();

        boolean isSubscriptionFound = subscriptionRepository
            .findSubscription(chatId, linkId)
            .isPresent();

        assertThat(isSubscriptionFound).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    void findByLinkIdShouldReturnAllChatsTrackingLink() {
        Long linkId = nextLink("https://github.com");
        Long chatId1 = registerChat(1L);
        Long chatId2 = registerChat(2L);

        Long[] expectedSubscriptions = {
            chatId1,
            chatId2
        };
        for (Long chatId : expectedSubscriptions) {
            client.sql("INSERT INTO subscription VALUES(?, ?);")
                .params(chatId, linkId)
                .update();
        }

        List<Long> subscribers = subscriptionRepository
            .findByLinkId(linkId)
            .stream()
            .map(Chat::id)
            .toList();

        assertThat(subscribers).containsExactly(expectedSubscriptions);
    }

    @Test
    @Transactional
    @Rollback
    void findByChatIdShouldReturnAllChatsTrackingLink() {
        Long chatId = registerChat(1L);

        Long[] expectedLinkIds = {
            nextLink("https://github.com"),
            nextLink("https://stackoverflow.com")
        };
        for (Long linkId : expectedLinkIds) {
            client.sql("INSERT INTO subscription VALUES(?, ?);")
                .params(chatId, linkId)
                .update();
        }

        List<Long> foundLinksIds = subscriptionRepository
            .findByChatId(chatId)
            .stream()
            .map(Link::id)
            .toList();

        assertThat(foundLinksIds).containsExactly(expectedLinkIds);
    }
}

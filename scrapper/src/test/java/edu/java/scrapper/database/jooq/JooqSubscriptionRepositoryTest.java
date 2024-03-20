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
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JooqSubscriptionRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JooqSubscriptionRepository subscriptionRepository;
    @Autowired
    private DSLContext dslContext;

    @Test
    @Transactional
    @Rollback
    void addSubscriptionShouldInsertRecord() {
    }

    @Test
    @Transactional
    @Rollback
    void removeSubscriptionShouldDeleteRecord() {
    }

    @Test
    @Transactional
    @Rollback
    void findSubscriptionShouldReturnExpectedSubscription() {
    }

    @Test
    @Transactional
    @Rollback
    void findByLinkIdShouldReturnAllChatsTrackingLink() {
    }

    @Test
    @Transactional
    @Rollback
    void findByChatIdShouldReturnAllChatsTrackingLink() {
    }
}

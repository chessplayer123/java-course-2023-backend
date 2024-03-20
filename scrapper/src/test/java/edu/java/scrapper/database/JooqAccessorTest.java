package edu.java.scrapper.database;

import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.repository.jdbc.JdbcSubscriptionRepository;
import edu.java.repository.jooq.JooqChatRepository;
import edu.java.repository.jooq.JooqLinkRepository;
import edu.java.repository.jooq.JooqSubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JooqAccessorTest  extends IntegrationEnvironment {
    @Autowired
    private JooqLinkRepository linkRepository;
    @Autowired
    private JooqChatRepository chatRepository;
    @Autowired
    private JooqSubscriptionRepository subscriptionRepository;
    @Autowired
    private JdbcClient client;

    @Test
    @Transactional
    @Rollback
    void addTest() {
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
    }
}

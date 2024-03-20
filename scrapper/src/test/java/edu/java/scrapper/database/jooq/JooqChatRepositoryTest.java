package edu.java.scrapper.database.jooq;

import edu.java.repository.jooq.JooqChatRepository;
import edu.java.scrapper.database.IntegrationEnvironment;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JooqChatRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JooqChatRepository chatRepository;
    @Autowired
    private DSLContext dslContext;

    @Test
    @Transactional
    @Rollback
    void addChatShouldInsertRecord() {
    }

    @Test
    @Transactional
    @Rollback
    void removeChatByIdShouldDeleteRecord() {
    }

    @Test
    @Transactional
    @Rollback
    void findChatByIdShouldReturnChat() {
    }
}

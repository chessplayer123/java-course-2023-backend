package edu.java.scrapper.database.jooq;

import edu.java.repository.dto.Chat;
import edu.java.repository.jooq.JooqChatRepository;
import edu.java.scrapper.database.IntegrationEnvironment;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import static edu.java.domain.jooq.Tables.CHAT;
import static org.assertj.core.api.Assertions.assertThat;

public class JooqChatRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JooqChatRepository chatRepository;
    @Autowired
    private DSLContext dslContext;

    @Test
    @Transactional
    @Rollback
    void addChatShouldInsertRecord() {
        Chat chat = new Chat(31415L, OffsetDateTime.now());

        chatRepository.add(chat);

        boolean isChatAdded = dslContext.selectFrom(CHAT)
            .where(CHAT.ID.eq(chat.id()))
            .fetchOptional()
            .isPresent();

        assertThat(isChatAdded).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    void removeChatByIdShouldDeleteRecord() {
        Long chatId = 15L;

        dslContext.insertInto(CHAT, CHAT.ID)
            .values(chatId)
            .execute();

        chatRepository.remove(chatId);

        boolean isChatPresent = dslContext.selectFrom(CHAT)
            .where(CHAT.ID.eq(chatId))
            .fetchOptional()
            .isPresent();

        assertThat(isChatPresent).isFalse();
    }

    @Test
    @Transactional
    @Rollback
    void findChatByIdShouldReturnChat() {
        Long chatId = 404L;

        dslContext.insertInto(CHAT, CHAT.ID)
            .values(chatId)
            .execute();

        boolean isChatFound = chatRepository.findById(chatId).isPresent();

        assertThat(isChatFound).isTrue();
    }
}

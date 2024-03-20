package edu.java.scrapper.database.jdbc;

import edu.java.repository.dto.Chat;
import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.database.IntegrationEnvironment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JdbcChatRepositoryTest  extends IntegrationEnvironment {
    @Autowired
    private JdbcChatRepository chatRepository;
    @Autowired
    private JdbcClient client;

    @Test
    @Transactional
    @Rollback
    void addChatShouldInsertRecord() {
        Long chatId = 3141592L;
        OffsetDateTime date = OffsetDateTime.now();
        Chat registeredChat = new Chat(chatId, date);

        chatRepository.add(registeredChat);

        Optional<Chat> presentChat = client.sql("SELECT * from chat WHERE id = ?;")
            .params(chatId)
            .query(Chat.class)
            .optional();

        assertThat(presentChat)
            .isPresent();
    }

    @Test
    @Transactional
    @Rollback
    void removeChatByIdShouldDeleteRecord() {
        Long chatId = 271828L;

        client.sql("INSERT INTO chat(id) VALUES(?);")
            .params(chatId)
            .update();

        chatRepository.remove(chatId);

        boolean isChatPresent = client.sql("SELECT * from chat WHERE id = ?;")
            .params(chatId)
            .query(Chat.class)
            .optional()
            .isPresent();

        assertThat(isChatPresent).isFalse();
    }

    @Test
    @Transactional
    @Rollback
    void findChatByIdShouldReturnChat() {
        Long chatId = 271828L;

        client.sql("INSERT INTO chat(id) VALUES(?);")
            .params(chatId)
            .update();

        Optional<Chat> foundChat = chatRepository.findById(chatId);

        assertThat(foundChat).isPresent();
    }
}

package edu.java.repository.jdbc;

import edu.java.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {
    private final JdbcClient jdbcClient;

    @Override
    public void add(Long chatId) {
        jdbcClient.sql("INSERT INTO chat(id) VALUES(?);")
            .params(chatId)
            .update();
    }

    @Override
    public void remove(Long chatId) {
        jdbcClient.sql("DELETE FROM chat WHERE id = :id;")
            .param("id", chatId)
            .update();
    }

    @Override
    public boolean contains(Long chatId) {
        return jdbcClient.sql("SELECT id FROM chat WHERE id = :id;")
            .param("id", chatId)
            .query((rs, rowNum) -> rs.getLong(1))
            .optional()
            .isPresent();
    }
}

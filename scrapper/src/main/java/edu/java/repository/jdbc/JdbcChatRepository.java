package edu.java.repository.jdbc;

import edu.java.repository.ChatRepository;
import edu.java.repository.dto.Chat;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@Lazy
@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {
    private final JdbcClient jdbcClient;

    @Override
    public void add(Chat chat) {
        jdbcClient.sql("INSERT INTO chat(id, registered_at) VALUES(?, ?);")
            .params(chat.id(), chat.registeredAt())
            .update();
    }

    @Override
    public void remove(Long chatId) {
        jdbcClient.sql("DELETE FROM chat WHERE id = :id;")
            .param("id", chatId)
            .update();
    }

    @Override
    public Optional<Chat> findById(Long chatId) {
        return jdbcClient.sql("SELECT * FROM chat WHERE id = :id;")
            .param("id", chatId)
            .query(Chat.class)
            .optional();
    }
}

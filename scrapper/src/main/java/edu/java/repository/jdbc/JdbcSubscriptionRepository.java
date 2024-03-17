package edu.java.repository.jdbc;

import edu.java.repository.SubscriptionRepository;
import edu.java.repository.dto.Chat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcSubscriptionRepository implements SubscriptionRepository {
    private final JdbcClient jdbcClient;

    @Override
    public void add(Long chatId, Long linkId) {
        jdbcClient.sql("""
            INSERT INTO subscription (chat_id, link_id)
            VALUES(?, ?);
        """)
            .params(chatId, linkId)
            .update();
    }

    @Override
    public void remove(Long chatId, Long linkId) {
        jdbcClient.sql("DELETE FROM subscription WHERE chat_id = ? and link_id = ?;")
            .params(chatId, linkId)
            .update();
    }

    @Override
    public List<Chat> findAllSubscribers(Long linkId) {
        return jdbcClient.sql("SELECT chat_id FROM subscription WHERE link_id = ?;")
            .params(linkId)
            .query(Chat.class)
            .list();
    }

    @Override
    public boolean isLinkTrackedByChat(Long chatId, Long linkId) {
        return jdbcClient.sql("SELECT 1 FROM subscription WHERE chat_id = ? and link_id = ?;")
            .params(chatId, linkId)
            .query((rs, rowNum) -> rowNum)
            .optional()
            .isPresent();
    }

    @Override
    public boolean isAnyChatTrackingLink(Long linkId) {
        return jdbcClient.sql("SELECT 1 FROM subscription WHERE link_id = ?;")
            .params(linkId)
            .query((rs, rowNum) -> rowNum)
            .optional()
            .isPresent();
    }
}

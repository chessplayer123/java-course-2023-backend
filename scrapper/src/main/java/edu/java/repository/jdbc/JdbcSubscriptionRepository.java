package edu.java.repository.jdbc;

import edu.java.repository.SubscriptionRepository;
import edu.java.repository.dto.Chat;
import edu.java.repository.dto.Link;
import edu.java.repository.dto.Subscription;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@Lazy
@RequiredArgsConstructor
public class JdbcSubscriptionRepository implements SubscriptionRepository {
    private final JdbcClient jdbcClient;

    @Override
    public void add(Long chatId, Long linkId) {
        jdbcClient.sql("INSERT INTO subscription (chat_id, link_id) VALUES(?, ?); ")
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
    public Optional<Subscription> findSubscription(Long chatId, Long linkId) {
        return jdbcClient.sql("SELECT * FROM subscription WHERE chat_id = ? and link_id = ?;")
            .params(chatId, linkId)
            .query(Subscription.class)
            .optional();
    }

    @Override
    public List<Chat> findByLinkId(Long linkId) {
        return jdbcClient.sql("SELECT chat.* FROM chat JOIN subscription ON chat.id = chat_id and link_id = ?;")
            .params(linkId)
            .query(Chat.class)
            .list();
    }

    @Override
    public List<Link> findByChatId(Long chatId) {
        return jdbcClient.sql("""
            SELECT * FROM link
            JOIN subscription ON
                chat_id = ?
                and link.id = subscription.link_id;
        """)
            .params(chatId)
            .query(Link.class)
            .list();
    }

}

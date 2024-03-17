package edu.java.repository.jdbc;

import edu.java.repository.LinkRepository;
import edu.java.repository.dto.Link;
import edu.java.response.LinkApiResponse;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcClient jdbcClient;

    @Override
    public Long add(LinkApiResponse info) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("INSERT INTO link (url, data) VALUES(?, ?::json);")
            .params(info.getLink().toString(), info.serializeToJson())
            .update(keyHolder, "id");
        return keyHolder.getKeyAs(Long.class);
    }

    @Override
    public void remove(Long linkId) {
        jdbcClient.sql("DELETE FROM link WHERE id = :id;")
            .param("id", linkId)
            .query();
    }

    @Override
    public void update(Long linkId, LinkApiResponse info, OffsetDateTime updateTime) {
        jdbcClient.sql("UPDATE link SET data = ?, last_check_time = ?")
            .params(info.serializeToJson(), updateTime.toString())
            .update();
    }

    @Override
    public void prune() {
        jdbcClient.sql("DELETE FROM link WHERE NOT EXISTS (SELECT FROM subscription WHERE link_id = id);")
            .update();
    }

    @Override
    public Optional<Link> findByURL(String url) {
        return jdbcClient.sql("SELECT * FROM link WHERE url = :url;")
            .param("url", url)
            .query(Link.class)
            .optional();
    }

    @Override
    public List<Link> findByChat(Long chatId) {
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

    @Override
    public List<Link> findLastCheckTimeExceedLimit(Duration limit) {
        return jdbcClient.sql("SELECT * FROM link WHERE last_check_time < :time;")
            .param("time", OffsetDateTime.now().minus(limit))
            .query(Link.class)
            .list();
    }
}

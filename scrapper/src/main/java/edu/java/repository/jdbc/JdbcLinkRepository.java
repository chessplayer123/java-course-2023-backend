package edu.java.repository.jdbc;

import edu.java.repository.LinkRepository;
import edu.java.repository.dto.Link;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@Lazy
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcClient jdbcClient;

    @Override
    public Long add(Link link) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("INSERT INTO link (url, description, created_at, last_check_time) VALUES(?, ?, ?, ?);")
            .params(link.url().toString(), link.description(), link.createdAt(), link.lastCheckTime())
            .update(keyHolder, "id");
        return keyHolder.getKeyAs(Long.class);
    }

    @Override
    public void remove(Long linkId) {
        jdbcClient.sql("DELETE FROM link WHERE id = :id;")
            .param("id", linkId)
            .update();
    }

    @Override
    public void update(Long linkId, OffsetDateTime updateTime) {
        jdbcClient.sql("UPDATE link SET last_check_time = ? WHERE id = ?;")
            .params(updateTime, linkId)
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
    public List<Link> findLastCheckTimeExceedLimit(Duration limit) {
        return jdbcClient.sql("SELECT * FROM link WHERE last_check_time < :time;")
            .param("time", OffsetDateTime.now().minus(limit))
            .query(Link.class)
            .list();
    }
}

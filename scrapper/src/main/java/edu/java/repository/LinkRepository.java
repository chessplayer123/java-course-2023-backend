package edu.java.repository;

import edu.java.repository.dto.Link;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    Long add(URI url, String description);

    void remove(Long linkId);

    void update(Long linkId, OffsetDateTime updateTime);

    void prune();

    Optional<Link> findByURL(String url);

    List<Link> findByChat(Long chatId);

    List<Link> findLastCheckTimeExceedLimit(Duration limit);
}

package edu.java.repository;

import edu.java.repository.dto.Link;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    Long add(Link link);

    void remove(Long linkId);

    void update(Long linkId, OffsetDateTime updateTime);

    Optional<Link> findByURL(String url);

    List<Link> findLastCheckTimeExceedLimit(Duration limit);

    void prune();
}

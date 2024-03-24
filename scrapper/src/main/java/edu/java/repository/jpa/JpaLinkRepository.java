package edu.java.repository.jpa;

import edu.java.repository.jpa.entity.LinkEntity;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Lazy
public interface JpaLinkRepository extends JpaRepository<LinkEntity, Long> {
    Optional<LinkEntity> findByUrl(String url);

    List<LinkEntity> findAllByLastCheckTimeBefore(OffsetDateTime fromDate);

    @Modifying
    @Query(
        value = "DELETE FROM link WHERE NOT EXISTS (SELECT FROM subscription WHERE link_id = link.id)",
        nativeQuery = true
    )
    void prune();
}

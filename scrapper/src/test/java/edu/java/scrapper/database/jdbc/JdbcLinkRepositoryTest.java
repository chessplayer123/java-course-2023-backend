package edu.java.scrapper.database.jdbc;

import edu.java.repository.dto.Link;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.database.IntegrationEnvironment;
import org.jooq.impl.QOM;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {"app.scheduler.enable=false"})
public class JdbcLinkRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JdbcLinkRepository linkRepository;
    @Autowired
    private JdbcClient client;

    private Long insertLink(String url) {
        KeyHolder holder = new GeneratedKeyHolder();
        client.sql("INSERT INTO link(url, description, last_check_time) VALUES(?, ?, ?);")
            .params(url, "Text", OffsetDateTime.now())
            .update(holder, "id");
        return holder.getKeyAs(Long.class);
    }

    @Test
    @Transactional
    @Rollback
    void addLinkShouldInsertRecord() {
        Link link = Link.from(URI.create("https://github.com"), "Text", OffsetDateTime.now(), OffsetDateTime.now());

        Long linkId = linkRepository.add(link);

        boolean isLinkAdded = client.sql("SELECT * FROM link WHERE id = ?;")
            .params(linkId)
            .query(Link.class)
            .optional()
            .isPresent();

        assertThat(isLinkAdded).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    void removeLinkShouldDeleteRecord() {
        Long linkId = insertLink("https://google.com");

        linkRepository.remove(linkId);
        boolean isLinkPresent = client.sql("SELECT * FROM link WHERE id = ?;")
            .params(linkId)
            .query(Link.class)
            .optional()
            .isPresent();

        assertThat(isLinkPresent).isFalse();
    }

    @Test
    @Transactional
    @Rollback
    void updateShouldChangeRecord() {
        Long linkId = insertLink("https://google.com");

        OffsetDateTime newDate = OffsetDateTime.now();
        linkRepository.update(linkId, newDate);

        Link link = client.sql("SELECT * FROM link WHERE id = ?;")
            .params(linkId)
            .query(Link.class)
            .single();

        assertThat(link.lastCheckTime().truncatedTo(ChronoUnit.MILLIS))
            .isEqualTo(newDate.truncatedTo(ChronoUnit.MILLIS));
    }

    @Test
    @Transactional
    @Rollback
    void findByUrlShouldReturnExpectedLink() {
        URI url = URI.create("https://google.com");
        String description = "Description";
        KeyHolder holder = new GeneratedKeyHolder();
        client.sql("INSERT INTO link(url, description) VALUES(?, ?);")
            .params(url.toString(), description)
            .update(holder, "id");
        Long linkId = holder.getKeyAs(Long.class);

        Optional<Link> foundLink = linkRepository.findByURL(url.toString());

        assertThat(foundLink)
            .isPresent()
            .get()
            .extracting("id", "url", "description")
            .containsExactly(linkId, url, description);
    }

    @Test
    @Transactional
    @Rollback
    void pruneShouldDeleteAllOrphansLinks() {
        Long linkId1 = insertLink("https://github.com");
        Long linkId2 = insertLink("https://stackoverflow.com");
        Long chatId = 0L;
        client.sql("INSERT INTO chat(id) VALUES(?);")
            .params(chatId)
            .update();
        client.sql("INSERT INTO subscription(chat_id, link_id) VALUES(?, ?);")
            .params(chatId, linkId1)
            .update();

        linkRepository.prune();

        List<Long> remainedLinksIds = client.sql("SELECT * FROM link;")
            .query(Link.class)
            .stream()
            .map(Link::id)
            .toList();

        assertThat(remainedLinksIds)
            .containsExactly(linkId1)
            .doesNotContain(linkId2);
    }

    @Test
    @Transactional
    @Rollback
    void findLastCheckTimeExceedLimitShouldReturnAllOldLinks() {
        Duration limit = Duration.ofHours(1);
        OffsetDateTime date1 = OffsetDateTime.now().minus(Duration.ofDays(1));
        OffsetDateTime date2 = OffsetDateTime.now();

        KeyHolder holder = new GeneratedKeyHolder();
        client.sql("INSERT INTO link(url, description, last_check_time) VALUES(?, ?, ?);")
            .params("https://gtihub.com", "text", date1)
            .update(holder, "id");
        client.sql("INSERT INTO link(url, description, last_check_time) VALUES(?, ?, ?);")
            .params("https://stackoverflow.com", "text", date2)
            .update();

        Long[] expectedLinksIds = {holder.getKeyAs(Long.class)};

        List<Long> actualLinksIds = linkRepository
            .findLastCheckTimeExceedLimit(limit)
            .stream()
            .map(Link::id)
            .toList();

        assertThat(actualLinksIds).containsExactly(expectedLinksIds);
    }
}

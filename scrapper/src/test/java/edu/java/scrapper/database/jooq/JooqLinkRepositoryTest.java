package edu.java.scrapper.database.jooq;

import edu.java.repository.dto.Link;
import edu.java.repository.jooq.JooqLinkRepository;
import edu.java.scrapper.database.IntegrationEnvironment;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import static edu.java.domain.jooq.Tables.CHAT;
import static edu.java.domain.jooq.Tables.LINK;
import static edu.java.domain.jooq.Tables.SUBSCRIPTION;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JooqLinkRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JooqLinkRepository linkRepository;
    @Autowired
    private DSLContext dslContext;


    @Test
    @Transactional
    @Rollback
    void addLinkShouldInsertRecord() {
        Link link = Link.from(URI.create("https://github.com"), "Text", OffsetDateTime.now(), OffsetDateTime.now());

        Long addedLinkId = linkRepository.add(link);

        boolean isLinkAdded = dslContext.selectFrom(LINK)
            .where(LINK.ID.eq(addedLinkId))
            .fetchOptional()
            .isPresent();

        assertThat(isLinkAdded).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    void removeLinkShouldDeleteRecord() {
        Long linkId = dslContext.insertInto(LINK, LINK.URL, LINK.DESCRIPTION)
            .values("https://youtube.com", "Description")
            .returningResult(LINK.ID)
            .fetchOneInto(Long.class);

        linkRepository.remove(linkId);

        boolean isLinkPresent = dslContext.selectFrom(LINK)
            .where(LINK.ID.eq(linkId))
            .fetchOptional()
            .isPresent();

        assertThat(isLinkPresent).isFalse();
    }

    @Test
    @Transactional
    @Rollback
    void updateShouldChangeRecord() {
        Long linkId = dslContext.insertInto(LINK, LINK.URL, LINK.DESCRIPTION, LINK.LAST_CHECK_TIME)
            .values("https://youtube.com", "description", OffsetDateTime.now())
            .returningResult(LINK.ID)
            .fetchOneInto(Long.class);

        OffsetDateTime updatedDate = OffsetDateTime.now().plus(Duration.ofDays(10));

        linkRepository.update(linkId, updatedDate);

        Link updatedLink = dslContext.selectFrom(LINK)
            .where(LINK.ID.eq(linkId))
            .fetchOneInto(Link.class);

        assertThat(updatedLink.lastCheckTime().truncatedTo(ChronoUnit.MILLIS))
            .isEqualTo(updatedDate.truncatedTo(ChronoUnit.MILLIS));
    }

    @Test
    @Transactional
    @Rollback
    void findByUrlShouldReturnExpectedLink() {
        String url = "https://jooq.org";
        Long linkId = dslContext.insertInto(LINK, LINK.URL, LINK.DESCRIPTION)
            .values(url, "Description")
            .returningResult(LINK.ID)
            .fetchOneInto(Long.class);

        Optional<Link> foundLink = linkRepository.findByURL(url);

        assertThat(foundLink)
            .isPresent()
            .get()
            .extracting(Link::id, Link::url)
            .containsExactly(linkId, URI.create(url));
    }

    @Test
    @Transactional
    @Rollback
    void pruneShouldDeleteAllOrphansLinks() {
        Long chatId = 0L;

        Long remainingLinkId = dslContext.insertInto(LINK, LINK.URL, LINK.DESCRIPTION)
            .values("https://github.com", "description")
            .returningResult(LINK.ID)
            .fetchOneInto(Long.class);
        dslContext.insertInto(LINK, LINK.URL, LINK.DESCRIPTION)
            .values("https://stackoverflow.com", "description")
            .execute();
        dslContext.insertInto(LINK, LINK.URL, LINK.DESCRIPTION)
            .values("https://google.com", "description")
            .execute();

        dslContext.insertInto(CHAT, CHAT.ID)
            .values(chatId)
            .execute();
        dslContext.insertInto(SUBSCRIPTION)
            .values(chatId, remainingLinkId)
            .execute();

        linkRepository.prune();

        List<Long> remainedLinksIds = dslContext.select(LINK.ID)
            .from(LINK)
            .fetchInto(Long.class);

        assertThat(remainedLinksIds)
            .containsExactly(remainingLinkId);
    }

    @Test
    @Transactional
    @Rollback
    void findLastCheckTimeExceedLimitShouldReturnAllOldLinks() {
        Duration duration = Duration.ofHours(1);
        Long expectedLinkId = dslContext.insertInto(LINK, LINK.URL, LINK.DESCRIPTION, LINK.LAST_CHECK_TIME)
            .values("https://youtube.com", "Description", OffsetDateTime.now().minus(Duration.ofDays(1)))
            .returningResult(LINK.ID)
            .fetchOneInto(Long.class);
        dslContext.insertInto(LINK, LINK.URL, LINK.DESCRIPTION, LINK.LAST_CHECK_TIME)
            .values("https://google.com", "Description", OffsetDateTime.now())
            .execute();

        List<Long> actualLinkIds = linkRepository
            .findLastCheckTimeExceedLimit(duration)
            .stream()
            .map(Link::id)
            .toList();

        assertThat(actualLinkIds)
            .containsExactly(expectedLinkId);
    }
}

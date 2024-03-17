package edu.java.repository.jooq;

import edu.java.repository.LinkRepository;
import edu.java.repository.dto.Link;
import edu.java.response.LinkApiResponse;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.Tables.LINK;
import static edu.java.domain.jooq.Tables.SUBSCRIPTION;
import static org.jooq.JSONB.jsonb;

@Repository
@RequiredArgsConstructor
public class JooqLinkRepository implements LinkRepository {
    private final DSLContext dslContext;

    @Override
    public Long add(LinkApiResponse info) {
        return dslContext.insertInto(LINK, LINK.URL, LINK.DATA)
            .values(info.getLink().toString(), jsonb(info.serializeToJson()))
            .returning(LINK.ID)
            .fetchOneInto(Long.class);
    }

    @Override
    public void remove(Long linkId) {
        dslContext.deleteFrom(LINK)
            .where(LINK.ID.equal(linkId))
            .execute();
    }

    @Override
    public void update(Long linkId, LinkApiResponse info, OffsetDateTime updateTime) {
        dslContext.update(LINK)
            .set(LINK.DATA, jsonb(info.serializeToJson()))
            .set(LINK.LAST_CHECK_TIME, updateTime)
            .where(LINK.ID.equal(linkId))
            .execute();
    }

    @Override
    public void prune() {
        dslContext.deleteFrom(LINK)
            .whereNotExists(dslContext
                .selectFrom(SUBSCRIPTION)
                .where(SUBSCRIPTION.LINK_ID.equal(LINK.ID))
            )
            .execute();
    }

    @Override
    public Optional<Link> findByURL(String url) {
        return dslContext.select(LINK.fields())
            .from(LINK)
            .where(LINK.URL.equal(url))
            .fetchOptionalInto(Link.class);
    }

    @Override
    public List<Link> findByChat(Long chatId) {
        return dslContext.select(LINK.fields())
            .from(LINK)
            .join(SUBSCRIPTION)
            .on(SUBSCRIPTION.CHAT_ID.equal(chatId))
            .and(SUBSCRIPTION.LINK_ID.equal(LINK.ID))
            .fetchInto(Link.class);
    }

    @Override
    public List<Link> findLastCheckTimeExceedLimit(Duration limit) {
        return dslContext.select(LINK.fields())
            .from(LINK)
            .where(LINK.LAST_CHECK_TIME.lessThan(OffsetDateTime.now().minus(limit)))
            .fetchInto(Link.class);
    }
}

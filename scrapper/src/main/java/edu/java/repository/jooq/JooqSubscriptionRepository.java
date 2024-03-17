package edu.java.repository.jooq;

import edu.java.repository.SubscriptionRepository;
import edu.java.repository.dto.Chat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.Tables.SUBSCRIPTION;

@Repository
@RequiredArgsConstructor
public class JooqSubscriptionRepository implements SubscriptionRepository {
    private final DSLContext dslContext;

    @Override
    public void add(Long chatId, Long linkId) {
        dslContext.insertInto(SUBSCRIPTION, SUBSCRIPTION.fields())
            .values(chatId, linkId)
            .execute();
    }

    @Override
    public void remove(Long chatId, Long linkId) {
        dslContext.deleteFrom(SUBSCRIPTION)
            .where(SUBSCRIPTION.CHAT_ID.equal(chatId))
            .and(SUBSCRIPTION.LINK_ID.equal(linkId))
            .execute();
    }

    @Override
    public List<Chat> findAllSubscribers(Long linkId) {
        return dslContext.select(SUBSCRIPTION.CHAT_ID)
            .from(SUBSCRIPTION)
            .where(SUBSCRIPTION.LINK_ID.equal(linkId))
            .fetchInto(Chat.class);
    }

    @Override
    public boolean isLinkTrackedByChat(Long chatId, Long linkId) {
        return dslContext.selectFrom(SUBSCRIPTION)
            .where(SUBSCRIPTION.LINK_ID.equal(linkId))
            .and(SUBSCRIPTION.CHAT_ID.equal(chatId))
            .fetchOptional()
            .isPresent();
    }

    @Override
    public boolean isAnyChatTrackingLink(Long linkId) {
        return dslContext.selectFrom(SUBSCRIPTION)
            .where(SUBSCRIPTION.LINK_ID.equal(linkId))
            .fetchOptional()
            .isPresent();
    }
}

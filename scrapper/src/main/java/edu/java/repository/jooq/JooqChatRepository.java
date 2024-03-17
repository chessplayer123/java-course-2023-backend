package edu.java.repository.jooq;

import edu.java.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.Tables.CHAT;

@Repository
@RequiredArgsConstructor
public class JooqChatRepository implements ChatRepository {
    private final DSLContext dslContext;

    @Override
    public void add(Long chatId) {
        dslContext.insertInto(CHAT, CHAT.ID)
            .values(chatId)
            .execute();
    }

    @Override
    public void remove(Long chatId) {
        dslContext.deleteFrom(CHAT)
            .where(CHAT.ID.equal(chatId))
            .execute();
    }

    @Override
    public boolean contains(Long chatId) {
        return dslContext.selectFrom(CHAT)
            .where(CHAT.ID.equal(chatId))
            .fetchOptional()
            .isPresent();
    }
}

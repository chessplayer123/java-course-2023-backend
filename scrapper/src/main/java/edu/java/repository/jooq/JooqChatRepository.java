package edu.java.repository.jooq;

import edu.java.repository.ChatRepository;
import edu.java.repository.dto.Chat;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.Tables.CHAT;

@Repository
@RequiredArgsConstructor
public class JooqChatRepository implements ChatRepository {
    private final DSLContext dslContext;

    @Override
    public void add(Chat chat) {
        dslContext.insertInto(CHAT)
            .values(chat)
            .execute();
    }

    @Override
    public void remove(Long chatId) {
        dslContext.deleteFrom(CHAT)
            .where(CHAT.ID.equal(chatId))
            .execute();
    }

    @Override
    public Optional<Chat> findById(Long chatId) {
        return dslContext.selectFrom(CHAT)
            .where(CHAT.ID.equal(chatId))
            .fetchOptionalInto(Chat.class);
    }
}

package edu.java.scrapper.service.jpa;

import edu.java.exceptions.ChatIsNotRegisteredException;
import edu.java.exceptions.ReAddingChatException;
import edu.java.repository.dto.Chat;
import edu.java.repository.jpa.JpaChatRepository;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.repository.jpa.entity.ChatEntity;
import edu.java.repository.jpa.entity.LinkEntity;
import edu.java.scrapper.database.IntegrationEnvironment;
import edu.java.service.jpa.JpaChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.C;
import java.time.OffsetDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JpaChatServiceTest extends IntegrationEnvironment {
    @Autowired
    private JpaChatService chatService;
    @Autowired
    private JpaChatRepository chatRepository;
    @Autowired
    private JpaLinkRepository linkRepository;

    @Test
    @Transactional
    @Rollback
    public void registerShouldAddChatToRepository() {
        Long chatId = 3L;
        chatService.register(chatId);

        boolean isChatAdded = chatRepository.findById(chatId).isPresent();
        assertThat(isChatAdded).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    public void registerShouldThrowExceptionIfChatAlreadyRegistered() {
        Long chatId = 10L;
        chatRepository.save(new ChatEntity(chatId));

        assertThatThrownBy(() -> chatService.register(chatId))
            .isInstanceOf(ReAddingChatException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void unregisterShouldRemoveChatFromRepository() {
        Long chatId = 1L;
        chatRepository.save(new ChatEntity(chatId));
        chatService.unregister(chatId);

        boolean isChatPresent = chatRepository.findById(chatId).isPresent();
        assertThat(isChatPresent).isFalse();
    }

    @Test
    @Transactional
    @Rollback
    public void unregisterShouldRemoveAllOrphansLinks() {
        LinkEntity linkEntity1 = linkRepository.save(
            new LinkEntity("https://github.com", "description", OffsetDateTime.now(), OffsetDateTime.now())
        );
        linkRepository.save(
            new LinkEntity("https://stackoverflow.com", "description", OffsetDateTime.now(), OffsetDateTime.now())
        );
        Long chatId = 1L;
        chatRepository.save(new ChatEntity(chatId)).addLink(linkEntity1);

        chatService.unregister(chatId);

        assertThat(chatRepository.findById(chatId)).isEmpty();
        assertThat(linkRepository.findAll()).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    public void unregisterShouldThrowExceptionIfChatIsNotRegistered() {
        assertThatThrownBy(() -> chatService.unregister(0L))
            .isInstanceOf(ChatIsNotRegisteredException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void findChatsTrackingLinksShouldReturnAllChats() {
        ChatEntity chat1 = chatRepository.save(new ChatEntity(0L));
        ChatEntity chat2 = chatRepository.save(new ChatEntity(1L));
        LinkEntity linkEntity1 = linkRepository.save(
            new LinkEntity("https://github.com", "description", OffsetDateTime.now(), OffsetDateTime.now())
        );
        chat1.addLink(linkEntity1);
        chat2.addLink(linkEntity1);

        List<Chat> actualList = chatService.findChatsTrackingLink(linkEntity1.getId());

        assertThat(actualList)
            .containsExactlyInAnyOrder(chat1.toDto(), chat2.toDto());
    }
}

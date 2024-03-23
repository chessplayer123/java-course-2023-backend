package edu.java.scrapper.service;

import edu.java.exceptions.ChatIsNotRegisteredException;
import edu.java.exceptions.ReAddingChatException;
import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
import edu.java.repository.SubscriptionRepository;
import edu.java.repository.dto.Chat;
import edu.java.service.DefaultChatService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DefaultChatServiceTest {
    private final LinkRepository linkRepository = Mockito.mock(LinkRepository.class);
    private final ChatRepository chatRepository = Mockito.mock(ChatRepository.class);
    private final SubscriptionRepository subscriptionRepository = Mockito.mock(SubscriptionRepository.class);
    private final DefaultChatService chatService = new DefaultChatService(
        linkRepository,
        subscriptionRepository,
        chatRepository
    );

    @Test
    public void chatServiceRegisterShouldThrowExceptionOnChatReAdding() {
        Long chatId = 31828L;

        Mockito.when(chatRepository.findById(chatId))
                .thenReturn(Optional.of(new Chat(chatId, OffsetDateTime.now())));

        assertThatThrownBy(() -> chatService.register(chatId))
            .isInstanceOf(ReAddingChatException.class);
    }

    @Test
    public void chatServiceUnregisterShouldThrowExceptionIfChatIsAbsent() {
        Long chatId = 31828L;

        Mockito.when(chatRepository.findById(chatId))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> chatService.unregister(chatId))
            .isInstanceOf(ChatIsNotRegisteredException.class);
    }

    @Test
    public void findChatsTrackingLinkShouldCallToSubscriptionRepositoryMethod() {
        Long linkId = 0L;

        Mockito.when(subscriptionRepository.findByLinkId(linkId))
            .thenReturn(Collections.emptyList());

        chatService.findChatsTrackingLink(linkId);

        Mockito.verify(subscriptionRepository)
            .findByLinkId(linkId);
    }
}

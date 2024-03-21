package edu.java.scrapper.service;

import edu.java.exceptions.ChatIsNotRegisteredException;
import edu.java.exceptions.LinkIsNotPresentException;
import edu.java.exceptions.ReAddingLinkException;
import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
import edu.java.repository.SubscriptionRepository;
import edu.java.repository.dto.Chat;
import edu.java.repository.dto.Link;
import edu.java.repository.dto.Subscription;
import edu.java.service.DefaultLinkService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.C;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DefaultLinkServiceTest {
    private final LinkRepository linkRepository = Mockito.mock(LinkRepository.class);
    private final ChatRepository chatRepository = Mockito.mock(ChatRepository.class);
    private final SubscriptionRepository subscriptionRepository = Mockito.mock(SubscriptionRepository.class);
    private final DefaultLinkService linkService = new DefaultLinkService(
        linkRepository,
        subscriptionRepository,
        chatRepository
    );

    @Test
    public void trackMethodShouldThrowExceptionIfChatIsNotRegistered() {
        Long chatId = 1L;
        URI url = URI.create("https://github.com");
        String description = "description";

        Mockito.when(chatRepository.findById(chatId))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> linkService.track(chatId, url, description))
            .isInstanceOf(ChatIsNotRegisteredException.class);
    }

    @Test
    public void trackMethodShouldReturnIdOfPresentLinkIfItWasAlreadyStored() {
        Long chatId = 1L;
        Long presentLinkId = 345L;
        URI url = URI.create("https://github.com");
        String description = "description";

        Mockito.when(chatRepository.findById(chatId))
            .thenReturn(Optional.of(new Chat(chatId, OffsetDateTime.now())));

        Mockito.when(linkRepository.findByURL(url.toString()))
            .thenReturn(Optional.of(new Link(presentLinkId, url, description, OffsetDateTime.now(), OffsetDateTime.now())));

        Long returnedLinkId = linkService.track(chatId, url, description);

        assertThat(returnedLinkId).isEqualTo(presentLinkId);
    }

    @Test
    public void trackMethodShouldThrowExceptionIfLinkIsAlreadyTracked() {
        Long chatId = 1L;
        Long linkId = 1L;
        URI url = URI.create("https://github.com");
        String description = "description";

        Mockito.when(chatRepository.findById(chatId))
            .thenReturn(Optional.of(new Chat(chatId, OffsetDateTime.now())));

        Mockito.when(linkRepository.findByURL(url.toString()))
            .thenReturn(Optional.of(new Link(linkId, url, description, OffsetDateTime.now(), OffsetDateTime.now())));

        Mockito.when(subscriptionRepository.findSubscription(chatId, linkId))
            .thenReturn(Optional.of(new Subscription(chatId, linkId)));

        assertThatThrownBy(() -> linkService.track(chatId, url, description))
            .isInstanceOf(ReAddingLinkException.class);
    }

    @Test
    public void untrackMethodShouldThrowExceptionIfLinkIsNotTracked() {
        Long chatId = 10L;
        URI url = URI.create("https://google.com");

        Mockito.when(chatRepository.findById(chatId))
            .thenReturn(Optional.of(new Chat(chatId, OffsetDateTime.now())));

        Mockito.when(linkRepository.findByURL(url.toString()))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> linkService.untrack(chatId, url))
            .isInstanceOf(LinkIsNotPresentException.class);
    }

    @Test
    public void untrackMethodShouldThrowExceptionIfChatIsNotRegistered() {
        Long chatId = 10L;
        URI url = URI.create("https://google.com");

        assertThatThrownBy(() -> linkService.untrack(chatId, url))
            .isInstanceOf(ChatIsNotRegisteredException.class);
    }

    @Test
    public void listAllShouldThrowExceptionIfChatIsNotRegistered() {
        Long chatId = 10L;

        assertThatThrownBy(() -> linkService.listAll(chatId))
            .isInstanceOf(ChatIsNotRegisteredException.class);
    }

    @Test
    public void listAllShouldCallSubscriptionRepositoryMethod() {
        Long chatId = 10L;

        Mockito.when(chatRepository.findById(chatId))
            .thenReturn(Optional.of(new Chat(chatId, OffsetDateTime.now())));

        linkService.listAll(chatId);

        Mockito.verify(subscriptionRepository)
            .findByChatId(chatId);
    }
}


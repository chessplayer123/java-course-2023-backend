package edu.java.service;

import edu.java.exceptions.ChatIsNotRegisteredException;
import edu.java.exceptions.LinkIsNotPresentException;
import edu.java.exceptions.ReAddingLinkException;
import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
import edu.java.repository.SubscriptionRepository;
import edu.java.repository.dto.Link;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultLinkService implements LinkService {
    private final LinkRepository linkRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ChatRepository chatRepository;

    @Override
    public Long track(
        Long chatId,
        URI url,
        String description
    ) throws ReAddingLinkException, ChatIsNotRegisteredException {
        if (chatRepository.findById(chatId).isEmpty()) {
            throw new ChatIsNotRegisteredException();
        }

        Optional<Link> presentLink = linkRepository.findByURL(url.toString());
        if (presentLink.isPresent()) {
            Link link = presentLink.get();
            if (subscriptionRepository.findSubscription(chatId, link.id()).isPresent()) {
                throw new ReAddingLinkException();
            }
            subscriptionRepository.add(chatId, link.id());
            return link.id();
        }

        OffsetDateTime currentTime = OffsetDateTime.now();
        Link addedLink = Link.from(url, description, currentTime, currentTime);
        Long addedLinkId = linkRepository.add(addedLink);
        subscriptionRepository.add(chatId, addedLinkId);
        return addedLinkId;
    }

    @Override
    public Long untrack(Long chatId, URI url) throws ChatIsNotRegisteredException, LinkIsNotPresentException {
        if (chatRepository.findById(chatId).isEmpty()) {
            throw new ChatIsNotRegisteredException();
        }

        Link trackedLink = linkRepository
            .findByURL(url.toString())
            .orElseThrow(LinkIsNotPresentException::new);
        if (subscriptionRepository.findSubscription(chatId, trackedLink.id()).isEmpty()) {
            throw new LinkIsNotPresentException();
        }

        subscriptionRepository.remove(chatId, trackedLink.id());
        linkRepository.prune();
        return trackedLink.id();
    }

    @Override
    public Collection<Link> listAll(Long chatId) throws ChatIsNotRegisteredException {
        if (chatRepository.findById(chatId).isEmpty()) {
            throw new ChatIsNotRegisteredException();
        }
        return subscriptionRepository.findByChatId(chatId);
    }

    @Override
    public void update(Long linkId, OffsetDateTime updateTime) throws LinkIsNotPresentException {
        linkRepository.update(linkId, updateTime);
    }

    @Override
    public Collection<Link> getLinksCheckTimeExceedLimit(Duration limit) {
        return linkRepository.findLastCheckTimeExceedLimit(limit);
    }
}

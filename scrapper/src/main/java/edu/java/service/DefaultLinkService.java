package edu.java.service;

import edu.java.exceptions.ChatIsNotRegisteredException;
import edu.java.exceptions.LinkIsNotPresentException;
import edu.java.exceptions.ReAddingLinkException;
import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
import edu.java.repository.SubscriptionRepository;
import edu.java.repository.dto.Link;
import edu.java.response.LinkApiResponse;
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
    public Long track(Long chatId, LinkApiResponse info) throws ReAddingLinkException, ChatIsNotRegisteredException {
        if (!chatRepository.contains(chatId)) {
            throw new ChatIsNotRegisteredException();
        }

        Optional<Link> presentLink = linkRepository.findByURL(info.getLink().toString());
        if (presentLink.isPresent()) {
            Link link = presentLink.get();
            if (subscriptionRepository.isLinkTrackedByChat(chatId, link.id())) {
                throw new ReAddingLinkException();
            }
            subscriptionRepository.add(chatId, link.id());
            return link.id();
        }

        Long addedLinkId = linkRepository.add(info);
        subscriptionRepository.add(chatId, addedLinkId);
        return addedLinkId;
    }

    @Override
    public Long untrack(Long chatId, URI url) throws ChatIsNotRegisteredException, LinkIsNotPresentException {
        if (!chatRepository.contains(chatId)) {
            throw new ChatIsNotRegisteredException();
        }

        Link trackedLink = linkRepository
            .findByURL(url.toString())
            .orElseThrow(LinkIsNotPresentException::new);
        if (!subscriptionRepository.isLinkTrackedByChat(chatId, trackedLink.id())) {
            throw new LinkIsNotPresentException();
        }

        subscriptionRepository.remove(chatId, trackedLink.id());
        if (!subscriptionRepository.isAnyChatTrackingLink(trackedLink.id())) {
            linkRepository.remove(trackedLink.id());
        }
        return trackedLink.id();
    }

    @Override
    public Collection<Link> listAll(Long chatId) throws ChatIsNotRegisteredException {
        if (!chatRepository.contains(chatId)) {
            throw new ChatIsNotRegisteredException();
        }
        return linkRepository.findByChat(chatId);
    }

    @Override
    public void update(Long linkId, LinkApiResponse updatedInfo, OffsetDateTime updateTime) {
        linkRepository.update(linkId, updatedInfo, updateTime);
    }

    @Override
    public Collection<Link> getLinksCheckTimeExceedLimit(Duration limit) {
        return linkRepository.findLastCheckTimeExceedLimit(limit);
    }
}

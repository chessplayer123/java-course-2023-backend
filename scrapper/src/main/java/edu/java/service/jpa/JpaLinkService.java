package edu.java.service.jpa;

import edu.java.exceptions.ChatIsNotRegisteredException;
import edu.java.exceptions.LinkIsNotPresentException;
import edu.java.exceptions.ReAddingLinkException;
import edu.java.repository.dto.Link;
import edu.java.repository.jpa.JpaChatRepository;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.repository.jpa.entity.ChatEntity;
import edu.java.repository.jpa.entity.LinkEntity;
import edu.java.service.LinkService;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final JpaLinkRepository linkRepository;
    private final JpaChatRepository chatRepository;

    @Override
    public Long track(
        Long chatId,
        URI url,
        String description
    ) throws ReAddingLinkException, ChatIsNotRegisteredException {
        ChatEntity chat = chatRepository.findById(chatId).orElseThrow(ChatIsNotRegisteredException::new);

        Optional<LinkEntity> presentLink = linkRepository.findByUrl(url.toString());
        if (presentLink.isPresent()) {
            LinkEntity link = presentLink.get();
            if (chat.getTrackedLinks().contains(link)) {
                throw new ReAddingLinkException();
            }
            chat.addLink(link);
            return link.getId();
        }

        OffsetDateTime currentTime = OffsetDateTime.now();
        LinkEntity addedLink = linkRepository.save(
            new LinkEntity(url.toString(), description, currentTime, currentTime)
        );
        chat.addLink(addedLink);
        return addedLink.getId();
    }

    @Override
    public Long untrack(Long chatId, URI url) throws ChatIsNotRegisteredException, LinkIsNotPresentException {
        ChatEntity chat = chatRepository
            .findById(chatId)
            .orElseThrow(ChatIsNotRegisteredException::new);

        LinkEntity trackedLink = linkRepository
            .findByUrl(url.toString())
            .orElseThrow(LinkIsNotPresentException::new);

        if (!chat.getTrackedLinks().contains(trackedLink)) {
            throw new LinkIsNotPresentException();
        }

        chat.removeLink(trackedLink);
        if (trackedLink.getSubscribedChats().isEmpty()) {
            linkRepository.delete(trackedLink);
        }
        return trackedLink.getId();
    }

    @Override
    public Collection<Link> listAll(Long chatId) throws ChatIsNotRegisteredException {
        return chatRepository
            .findById(chatId)
            .orElseThrow(ChatIsNotRegisteredException::new)
            .getTrackedLinks()
            .stream()
            .map(LinkEntity::toDto)
            .toList();
    }

    @Override
    public void update(Long linkId, OffsetDateTime updateTime) throws LinkIsNotPresentException {
        linkRepository
            .findById(linkId)
            .orElseThrow(LinkIsNotPresentException::new)
            .setLastCheckTime(updateTime);
    }

    @Override
    public Collection<Link> getLinksCheckTimeExceedLimit(Duration limit) {
        return linkRepository
            .findAllByLastCheckTimeBefore(OffsetDateTime.now().minus(limit))
            .stream()
            .map(LinkEntity::toDto)
            .toList();
    }
}

package edu.java.repository;

import edu.java.exceptions.LinkIsNotPresentException;
import edu.java.exceptions.ReAddingLinkException;
import edu.java.exceptions.ReAddingUserException;
import edu.java.exceptions.UserIsNotRegisteredException;
import edu.java.response.LinkInfo;
import edu.java.service.LinkService;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryRepository {
    private final Map<Long, Set<LinkService.Link>> links = new HashMap<>();
    private Long linkId = 0L;

    public void addUser(Long chatId) throws ReAddingUserException {
        if (links.containsKey(chatId)) {
            throw new ReAddingUserException();
        }
        links.put(chatId, new HashSet<>());
    }

    public void removeUser(Long chatId) throws UserIsNotRegisteredException {
        if (!links.containsKey(chatId)) {
            throw new UserIsNotRegisteredException();
        }
        links.remove(chatId);
    }

    public LinkService.Link addLink(
        Long chatId,
        LinkInfo info
    ) throws UserIsNotRegisteredException, ReAddingLinkException {
        if (!links.containsKey(chatId)) {
            throw new UserIsNotRegisteredException();
        } else if (links.get(chatId).stream().anyMatch(link -> link.info().getLink().equals(info.getLink()))) {
            throw new ReAddingLinkException();
        }
        LinkService.Link link = new LinkService.Link(linkId++, info);
        links.get(chatId).add(link);
        return link;
    }

    public LinkService.Link removeLink(
        Long chatId,
        URI url
    ) throws UserIsNotRegisteredException, LinkIsNotPresentException {
        if (!links.containsKey(chatId)) {
            throw new UserIsNotRegisteredException();
        }
        Set<LinkService.Link> userLinks = links.get(chatId);
        LinkService.Link removedLink = userLinks
            .stream()
            .filter(link -> link.info().getLink().equals(url))
            .findAny()
            .orElseThrow(LinkIsNotPresentException::new);
        userLinks.remove(removedLink);

        return removedLink;
    }

    public List<LinkService.Link> getTrackedLinks(Long chatId) throws UserIsNotRegisteredException {
        if (!links.containsKey(chatId)) {
            throw new UserIsNotRegisteredException();
        }
        return links.get(chatId).stream().toList();
    }
}

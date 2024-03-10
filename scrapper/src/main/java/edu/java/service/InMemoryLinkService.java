package edu.java.service;

import edu.java.exceptions.LinkIsNotPresentException;
import edu.java.exceptions.ReAddingLinkException;
import edu.java.exceptions.UserIsNotRegisteredException;
import edu.java.repository.InMemoryRepository;
import edu.java.response.LinkInfo;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InMemoryLinkService implements LinkService {
    private final InMemoryRepository repository;

    @Override
    public Link track(Long chatId, LinkInfo url) throws UserIsNotRegisteredException, ReAddingLinkException {
        return repository.addLink(chatId, url);
    }

    @Override
    public Link untrack(Long chatId, URI url) throws UserIsNotRegisteredException, LinkIsNotPresentException {
        return repository.removeLink(chatId, url);
    }

    @Override
    public List<Link> listAll(Long chatId) throws UserIsNotRegisteredException {
        return repository.getTrackedLinks(chatId);
    }
}

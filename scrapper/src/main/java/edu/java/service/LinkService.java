package edu.java.service;

import edu.java.exceptions.ChatIsNotRegisteredException;
import edu.java.exceptions.LinkIsNotPresentException;
import edu.java.exceptions.ReAddingLinkException;
import edu.java.repository.dto.Link;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import org.springframework.transaction.annotation.Transactional;

public interface LinkService {
    @Transactional
    Long track(Long chatId, URI info, String description) throws ReAddingLinkException, ChatIsNotRegisteredException;

    @Transactional
    Long untrack(Long chatId, URI url) throws ChatIsNotRegisteredException, LinkIsNotPresentException;

    @Transactional
    Collection<Link> listAll(Long chatId) throws ChatIsNotRegisteredException;

    @Transactional
    void update(Long linkId, OffsetDateTime updateTime) throws LinkIsNotPresentException;

    Collection<Link> getLinksCheckTimeExceedLimit(Duration limit);

    default void updateNow(Long linkId) throws LinkIsNotPresentException {
        update(linkId, OffsetDateTime.now());
    }
}

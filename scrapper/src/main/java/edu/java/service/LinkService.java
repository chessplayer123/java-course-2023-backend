package edu.java.service;

import edu.java.exceptions.ChatIsNotRegisteredException;
import edu.java.exceptions.LinkIsNotPresentException;
import edu.java.exceptions.ReAddingLinkException;
import edu.java.repository.dto.Link;
import edu.java.response.LinkApiResponse;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import org.springframework.transaction.annotation.Transactional;

public interface LinkService {
    @Transactional
    Long track(Long chatId, LinkApiResponse info) throws ReAddingLinkException, ChatIsNotRegisteredException;

    @Transactional
    Long untrack(Long chatId, URI url) throws ChatIsNotRegisteredException, LinkIsNotPresentException;

    @Transactional
    Collection<Link> listAll(Long chatId) throws ChatIsNotRegisteredException;

    @Transactional
    void update(Long linkId, LinkApiResponse updatedInfo, OffsetDateTime updateTime);

    Collection<Link> getLinksCheckTimeExceedLimit(Duration limit);

    default void updateNow(Long linkId, LinkApiResponse updatedInfo) {
        update(linkId, updatedInfo, OffsetDateTime.now());
    }
}

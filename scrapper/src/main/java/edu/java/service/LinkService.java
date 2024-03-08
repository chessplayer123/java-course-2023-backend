package edu.java.service;

import edu.java.exceptions.LinkIsNotPresentException;
import edu.java.exceptions.ReAddingLinkException;
import edu.java.exceptions.UserIsNotRegisteredException;
import edu.java.response.LinkInfo;
import java.net.URI;
import java.util.Collection;

public interface LinkService {
    Link add(Long chatId, LinkInfo url) throws ReAddingLinkException, UserIsNotRegisteredException;

    Link remove(Long chatId, URI url) throws UserIsNotRegisteredException, LinkIsNotPresentException;

    Collection<Link> listAll(Long chatId) throws UserIsNotRegisteredException;

    record Link(
        Long id,
        LinkInfo info
    ) {
    }
}

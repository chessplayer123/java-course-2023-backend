package edu.java.bot.user;

import edu.java.bot.link.Link;
import java.util.Set;

public interface UserService {
    void registerUser(long userId);

    Set<Link> getTrackedLinks(long userId) throws UserIsNotRegisteredException;

    void trackLink(long userId, Link link) throws UserIsNotRegisteredException;

    boolean unTrackLink(long userId, Link link) throws UserIsNotRegisteredException;
}

package edu.java.bot.user;

import edu.java.bot.exceptions.UserIsNotRegisteredException;
import edu.java.bot.link.Link;
import java.util.Set;

public interface UserService {
    boolean isUserRegistered(long userId);

    void registerUser(long userId);

    Set<Link> getTrackedLinks(long userId) throws UserIsNotRegisteredException;

    void trackLink(long userId, Link link) throws UserIsNotRegisteredException;

    void unTrackLink(long userId, Link link) throws UserIsNotRegisteredException;
}

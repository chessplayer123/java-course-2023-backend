package edu.java.bot.user;

import edu.java.bot.link.Link;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InMemoryUserService implements UserService {
    private final Map<Long, Set<Link>> users = new HashMap<>();

    @Override
    public void registerUser(long userId) {
        users.putIfAbsent(userId, new HashSet<>());
    }

    @Override
    public Set<Link> getTrackedLinks(long userId) throws UserIsNotRegisteredException {
        Set<Link> links = users.get(userId);
        if (links == null) {
            throw new UserIsNotRegisteredException();
        }
        return links;
    }

    @Override
    public void trackLink(long userId, Link link) throws UserIsNotRegisteredException {
        Set<Link> links = users.get(userId);
        if (links == null) {
            throw new UserIsNotRegisteredException();
        }
        links.add(link);
    }

    @Override
    public boolean unTrackLink(long userId, Link link) throws UserIsNotRegisteredException {
        Set<Link> links = users.get(userId);
        if (links == null) {
            throw new UserIsNotRegisteredException();
        }
        return links.remove(link);
    }
}

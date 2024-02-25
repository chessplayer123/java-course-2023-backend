package edu.java.bot.user;

import edu.java.bot.exceptions.UserIsNotRegisteredException;
import edu.java.bot.link.Link;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class InMemoryUserService implements UserService {
    private final InMemoryUserRepository repository = new InMemoryUserRepository();

    @Override
    public boolean isUserRegistered(long userId) {
        return repository.contains(userId);
    }

    @Override
    public void registerUser(long userId) {
        repository.add(new User(userId));
    }

    @Override
    public Set<Link> getTrackedLinks(long userId) throws UserIsNotRegisteredException {
        return repository
            .getOrThrow(userId, new UserIsNotRegisteredException())
            .getTrackedLinks();
    }

    @Override
    public void trackLink(long userId, Link link) throws UserIsNotRegisteredException {
        repository
            .getOrThrow(userId, new UserIsNotRegisteredException())
            .track(link);
    }

    @Override
    public void unTrackLink(long userId, Link link) throws UserIsNotRegisteredException {
        repository
            .getOrThrow(userId, new UserIsNotRegisteredException())
            .untrack(link);
    }
}

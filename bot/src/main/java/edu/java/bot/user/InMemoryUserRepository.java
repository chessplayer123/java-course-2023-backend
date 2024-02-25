package edu.java.bot.user;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public void add(User user) {
        users.putIfAbsent(user.getChatId(), user);
    }

    @Override
    public boolean contains(long userId) {
        return users.containsKey(userId);
    }

    @Override
    public User get(long userId) {
        return users.get(userId);
    }
}

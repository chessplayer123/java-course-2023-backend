package edu.java.bot.user;

public interface UserRepository {
    void add(User user);

    boolean contains(long userId);

    User get(long userId);

    default <E extends Exception> User getOrThrow(long userId, E exception) throws E {
        if (!contains(userId)) {
            throw exception;
        }
        return get(userId);
    }
}

package edu.java.bot.user;

import edu.java.bot.link.Link;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

@Getter
public class User {
    private final Long chatId;
    private final Set<Link> trackedLinks = new HashSet<>();

    public User(Long chatId) {
        this.chatId = chatId;
    }

    public void track(Link url) {
        trackedLinks.add(url);
    }

    public void untrack(Link url) {
        trackedLinks.remove(url);
    }
}

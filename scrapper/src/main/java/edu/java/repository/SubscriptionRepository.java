package edu.java.repository;

import edu.java.repository.dto.Chat;
import java.util.List;

public interface SubscriptionRepository {
    void add(Long chatId, Long linkId);

    void remove(Long chatId, Long linkId);

    List<Chat> findAllSubscribers(Long linkId);

    boolean isLinkTrackedByChat(Long chatId, Long linkId);

    boolean isAnyChatTrackingLink(Long linkId);
}

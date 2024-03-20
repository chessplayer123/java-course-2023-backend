package edu.java.repository;

import edu.java.repository.dto.Chat;
import edu.java.repository.dto.Subscription;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository {
    void add(Long chatId, Long linkId);

    void remove(Long chatId, Long linkId);

    Optional<Subscription> findSubscription(Long chatId, Long linkId);

    List<Chat> findAllSubscribers(Long linkId);
}

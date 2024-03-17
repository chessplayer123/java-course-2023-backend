package edu.java.service;

import edu.java.exceptions.ChatIsNotRegisteredException;
import edu.java.exceptions.ReAddingUserException;
import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
import edu.java.repository.SubscriptionRepository;
import edu.java.repository.dto.Chat;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultChatService implements ChatService {
    private final LinkRepository linkRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ChatRepository chatRepository;

    @Override
    public void register(Long chatId) throws ReAddingUserException {
        if (chatRepository.contains(chatId)) {
            throw new ReAddingUserException();
        }
        chatRepository.add(chatId);
    }

    @Override
    public void unregister(Long chatId) throws ChatIsNotRegisteredException {
        if (!chatRepository.contains(chatId)) {
            throw new ChatIsNotRegisteredException();
        }
        chatRepository.remove(chatId);
        linkRepository.prune();
    }

    @Override
    public List<Chat> findChatsTrackingLink(Long linkId) {
        return subscriptionRepository.findAllSubscribers(linkId).stream().toList();
    }
}

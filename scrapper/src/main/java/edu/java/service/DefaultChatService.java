package edu.java.service;

import edu.java.exceptions.ChatIsNotRegisteredException;
import edu.java.exceptions.ReAddingChatException;
import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
import edu.java.repository.SubscriptionRepository;
import edu.java.repository.dto.Chat;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultChatService implements ChatService {
    private final LinkRepository linkRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ChatRepository chatRepository;

    @Override
    public void register(Long chatId) throws ReAddingChatException {
        if (chatRepository.findById(chatId).isPresent()) {
            throw new ReAddingChatException();
        }
        chatRepository.add(new Chat(chatId, OffsetDateTime.now()));
    }

    @Override
    public void unregister(Long chatId) throws ChatIsNotRegisteredException {
        if (chatRepository.findById(chatId).isEmpty()) {
            throw new ChatIsNotRegisteredException();
        }
        chatRepository.remove(chatId);
        linkRepository.prune();
    }

    @Override
    public List<Chat> findChatsTrackingLink(Long linkId) {
        return subscriptionRepository.findByLinkId(linkId);
    }
}

package edu.java.service.jpa;

import edu.java.exceptions.ChatIsNotRegisteredException;
import edu.java.exceptions.LinkIsNotPresentException;
import edu.java.exceptions.ReAddingChatException;
import edu.java.repository.dto.Chat;
import edu.java.repository.jpa.JpaChatRepository;
import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.repository.jpa.entity.ChatEntity;
import edu.java.repository.jpa.entity.LinkEntity;
import edu.java.service.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaChatService implements ChatService {
    private final JpaLinkRepository linkRepository;
    private final JpaChatRepository chatRepository;

    @Override
    public void register(Long chatId) throws ReAddingChatException {
        if (chatRepository.findById(chatId).isPresent()) {
            throw new ReAddingChatException();
        }
        chatRepository.save(new ChatEntity(chatId));
    }

    @Override
    public void unregister(Long chatId) throws ChatIsNotRegisteredException {
        ChatEntity chat = chatRepository
            .findById(chatId)
            .orElseThrow(ChatIsNotRegisteredException::new);

        for (LinkEntity trackedLink : chat.getTrackedLinks()) {
            chat.removeLink(trackedLink);
            if (trackedLink.getSubscribedChats().isEmpty()) {
                linkRepository.delete(trackedLink);
            }
        }
        chatRepository.deleteById(chatId);
    }

    @Override
    public List<Chat> findChatsTrackingLink(Long linkId) throws LinkIsNotPresentException {
        return linkRepository
            .findById(linkId)
            .orElseThrow(LinkIsNotPresentException::new)
            .getSubscribedChats()
            .stream()
            .map(ChatEntity::toDto)
            .toList();
    }
}

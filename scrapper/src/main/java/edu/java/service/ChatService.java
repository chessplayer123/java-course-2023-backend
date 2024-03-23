package edu.java.service;

import edu.java.exceptions.ChatIsNotRegisteredException;
import edu.java.exceptions.ReAddingChatException;
import edu.java.repository.dto.Chat;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface ChatService {
    @Transactional
    void register(Long chatId) throws ReAddingChatException;

    @Transactional
    void unregister(Long chatId) throws ChatIsNotRegisteredException;

    @Transactional
    List<Chat> findChatsTrackingLink(Long linkId);
}

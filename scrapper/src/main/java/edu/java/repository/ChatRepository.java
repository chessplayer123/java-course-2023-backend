package edu.java.repository;

import edu.java.repository.dto.Chat;
import java.util.Optional;

public interface ChatRepository {
    void add(Long chatId);

    void remove(Long chatId);

    Optional<Chat> findById(Long chatId);
}

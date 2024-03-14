package edu.java.service;

import edu.java.exceptions.ReAddingUserException;
import edu.java.exceptions.UserIsNotRegisteredException;
import edu.java.repository.InMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InMemoryChatService implements ChatService {
    private final InMemoryRepository repository;

    @Override
    public void register(Long chatId) throws ReAddingUserException {
        repository.addUser(chatId);
    }

    @Override
    public void unregister(Long chatId) throws UserIsNotRegisteredException {
        repository.removeUser(chatId);
    }
}

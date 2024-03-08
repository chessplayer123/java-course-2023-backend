package edu.java.service;

import edu.java.exceptions.ReAddingUserException;
import edu.java.exceptions.UserIsNotRegisteredException;

public interface ChatService {
    void register(Long chatId) throws ReAddingUserException;

    void unregister(Long chatId) throws UserIsNotRegisteredException;
}

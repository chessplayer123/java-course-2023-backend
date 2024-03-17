package edu.java.repository;

public interface ChatRepository {
    void add(Long chatId);

    void remove(Long chatId);

    boolean contains(Long chatId);
}

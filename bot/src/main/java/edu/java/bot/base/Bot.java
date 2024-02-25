package edu.java.bot.base;

import com.pengrad.telegrambot.model.Update;
import java.util.List;

public interface Bot extends AutoCloseable {
    void start();

    int handleUpdates(List<Update> updates);
}

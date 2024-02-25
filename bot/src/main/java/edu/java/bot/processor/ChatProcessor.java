package edu.java.bot.processor;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface ChatProcessor {
    SendMessage process(Update update);

    BotCommand[] getBotCommands();
}

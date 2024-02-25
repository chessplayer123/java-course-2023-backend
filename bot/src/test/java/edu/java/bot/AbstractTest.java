package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.link.LinkProcessor;
import edu.java.bot.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {BotApplication.class})
public abstract class AbstractTest {
    @MockBean
    TelegramBot telegramBot;

    @Autowired
    protected UserService userService;

    @Autowired
    protected LinkProcessor linkProcessor;
}

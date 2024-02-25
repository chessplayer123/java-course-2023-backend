package edu.java.bot;

import edu.java.bot.link.LinkHandlerChain;
import edu.java.bot.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {BotApplication.class})
public abstract class AbstractTest {
    @Autowired
    protected UserService userService;

    @Autowired
    protected LinkHandlerChain handlerChain;
}

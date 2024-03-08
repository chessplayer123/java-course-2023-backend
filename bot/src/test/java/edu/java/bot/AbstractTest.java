package edu.java.bot;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.service.ScrapperUserService;
import edu.java.bot.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.WebServer;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(classes = {BotApplication.class})
public abstract class AbstractTest {
    @MockBean
    TelegramBot telegramBot;

    @MockBean
    UserService userService;
}

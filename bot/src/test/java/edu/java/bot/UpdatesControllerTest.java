package edu.java.bot;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.base.Bot;
import edu.java.bot.controller.UpdatesController;
import edu.java.bot.service.NotificationService;
import edu.java.bot.service.UserService;
import edu.java.dto.request.LinkUpdate;
import lombok.SneakyThrows;
import org.jose4j.jwk.Use;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UpdatesController.class)
public class UpdatesControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    NotificationService notificationService;

    @Test
    @SneakyThrows
    public void handleUpdatesShouldSendNotification() {
        mockMvc.perform(post("/updates")
                .content("""
                {
                    "id": 0,
                    "url": "https://github.com",
                    "date": "2023-10-10T14:37:45Z",
                    "description": "New update",
                    "tgChatIds": [3, 14, 15, 92]
                }
                """)
                .contentType("application/json")
            )
            .andExpect(status().isOk());

        LinkUpdate expectedUpdate = new LinkUpdate(
            0L,
            URI.create("https://github.com"),
            OffsetDateTime.parse("2023-10-10T14:37:45Z"),
            "New update",
            List.of(3L, 14L, 15L, 92L)
        );
        Mockito
            .verify(notificationService)
            .sendNotifications(expectedUpdate);
    }
}

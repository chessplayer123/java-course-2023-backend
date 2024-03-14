package edu.java.bot;

import edu.java.bot.base.Bot;
import edu.java.bot.controller.UpdatesController;
import edu.java.bot.service.UserService;
import lombok.SneakyThrows;
import org.jose4j.jwk.Use;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
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
    Bot bot;

    @Test
    @SneakyThrows
    public void handleUpdatesShouldSendNotification() {
        mockMvc.perform(post("/updates")
                .content("""
                {
                    "id": 0,
                    "url": "https://github.com",
                    "description": "New update",
                    "tgChatIds": [3, 14, 15, 92]
                }
                """)
                .contentType("application/json")
            )
            .andExpect(status().isOk());

        Mockito
            .verify(bot)
            .sendNotifications(List.of(3L, 14L, 15L, 92L), "New update");
    }
}

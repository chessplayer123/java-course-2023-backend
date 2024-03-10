package edu.java.scrapper;

import edu.java.controller.ChatController;
import edu.java.service.ChatService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
public class ChatControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ChatService chatService;

    @Test
    @SneakyThrows
    public void registerChatShouldCallChatServiceRegisterMethod() {
        Long chatId = 271828L;
        mockMvc
            .perform(post("/tg-chat/%d".formatted(chatId)))
            .andExpect(status().isOk());

        Mockito.verify(chatService).register(chatId);
    }

    @Test
    @SneakyThrows
    public void unregisterChatShouldCallChatServiceUnregisterMethod() {
        Long chatId = 271828L;
        mockMvc
            .perform(delete("/tg-chat/%d".formatted(chatId)))
            .andExpect(status().isOk());

        Mockito.verify(chatService).unregister(chatId);
    }
}

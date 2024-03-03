package edu.java.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
public class ChatBotController {
    @Operation(summary = "Delete chat with provided id")
    @DeleteMapping("/{id}")
    public void deleteChat(Long chatId) {
    }

    @Operation(summary = "Register new chat with provided id")
    @PostMapping("/{id}")
    public void registerChat(Long chatId) {
    }
}

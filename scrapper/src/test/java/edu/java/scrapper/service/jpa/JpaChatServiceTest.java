package edu.java.scrapper.service.jpa;

import edu.java.repository.jpa.JpaChatRepository;
import edu.java.scrapper.database.IntegrationEnvironment;
import edu.java.service.jpa.JpaChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class JpaChatServiceTest extends IntegrationEnvironment {
    @Autowired
    private JpaChatService chatService;
    @Autowired
    private JpaChatRepository chatRepository;

    @Test
    public void test() {
    }
}

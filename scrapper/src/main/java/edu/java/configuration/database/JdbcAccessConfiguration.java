package edu.java.configuration.database;

import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.repository.jdbc.JdbcSubscriptionRepository;
import edu.java.service.ChatService;
import edu.java.service.DefaultChatService;
import edu.java.service.DefaultLinkService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {
    @Bean
    public edu.java.service.LinkService jdbcLinkService(
        JdbcLinkRepository linkRepository,
        JdbcSubscriptionRepository subscriptionRepository,
        JdbcChatRepository chatRepository
    ) {
        return new DefaultLinkService(linkRepository, subscriptionRepository, chatRepository);
    }

    @Bean
    public ChatService jdbcChatService(
        JdbcLinkRepository linkRepository,
        JdbcSubscriptionRepository subscriptionRepository,
        JdbcChatRepository chatRepository
    ) {
        return new DefaultChatService(linkRepository, subscriptionRepository, chatRepository);
    }
}

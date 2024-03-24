package edu.java.configuration.database;

import edu.java.repository.jooq.JooqChatRepository;
import edu.java.repository.jooq.JooqLinkRepository;
import edu.java.repository.jooq.JooqSubscriptionRepository;
import edu.java.service.ChatService;
import edu.java.service.DefaultChatService;
import edu.java.service.DefaultLinkService;
import edu.java.service.LinkService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {
    @Bean
    public LinkService jooqLinkService(
        JooqLinkRepository linkRepository,
        JooqSubscriptionRepository subscriptionRepository,
        JooqChatRepository chatRepository
    ) {
        return new DefaultLinkService(linkRepository, subscriptionRepository, chatRepository);
    }

    @Bean
    public ChatService jooqChatService(
        JooqLinkRepository linkRepository,
        JooqSubscriptionRepository subscriptionRepository,
        JooqChatRepository chatRepository
    ) {
        return new DefaultChatService(linkRepository, subscriptionRepository, chatRepository);
    }
}

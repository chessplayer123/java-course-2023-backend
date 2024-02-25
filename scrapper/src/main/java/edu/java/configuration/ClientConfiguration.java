package edu.java.configuration;

import edu.java.client.github.repository.GHRepositoryClient;
import edu.java.client.stackoverflow.question.SOQuestionClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ClientConfiguration {
    @Bean
    public GHRepositoryClient githubRepositoryClient(ApplicationConfig config) {
        return new GHRepositoryClient(config.githubUrl());
    }

    @Bean
    public SOQuestionClient stackOverflowQuestionClient(ApplicationConfig config) {
        return new SOQuestionClient(config.stackoverflowUrl());
    }
}

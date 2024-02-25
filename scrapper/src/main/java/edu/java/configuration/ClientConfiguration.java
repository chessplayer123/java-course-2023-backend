package edu.java.configuration;

import edu.java.client.github.GithubClient;
import edu.java.client.stackoverflow.StackOverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ClientConfiguration {
    @Bean
    public GithubClient githubClient(ApplicationConfig config) {
        return new GithubClient(config.githubUrl());
    }

    @Bean
    public StackOverflowClient stackOverflowClient(ApplicationConfig config) {
        return new StackOverflowClient(config.stackoverflowUrl());
    }
}

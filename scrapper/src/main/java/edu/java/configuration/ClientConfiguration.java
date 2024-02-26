package edu.java.configuration;

import edu.java.client.github.GithubClient;
import edu.java.client.github.GithubRepositorySubClient;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.client.stackoverflow.StackOverflowQuestionSubClient;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ClientConfiguration {
    @Bean
    public GithubClient githubClient(ApplicationConfig config) {
        return new GithubClient(
            config.githubUrl(),
            List.of(new GithubRepositorySubClient())
        );
    }

    @Bean
    public StackOverflowClient stackOverflowClient(ApplicationConfig config) {
        return new StackOverflowClient(
            config.stackoverflowUrl(),
            List.of(new StackOverflowQuestionSubClient())
        );
    }
}

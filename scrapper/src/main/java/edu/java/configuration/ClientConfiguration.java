package edu.java.configuration;

import edu.java.client.Client;
import edu.java.client.github.GithubRepositorySubClient;
import edu.java.client.stackoverflow.StackOverflowQuestionSubClient;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ClientConfiguration {
    @Bean
    public Client githubClient(ApplicationConfig config) {
        return new Client(
            config.githubUrl(),
            List.of(new GithubRepositorySubClient())
        );
    }

    @Bean
    public Client stackOverflowClient(ApplicationConfig config) {
        return new Client(
            config.stackoverflowUrl(),
            List.of(new StackOverflowQuestionSubClient())
        );
    }
}

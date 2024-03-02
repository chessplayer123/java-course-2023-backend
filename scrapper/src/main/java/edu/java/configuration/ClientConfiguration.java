package edu.java.configuration;

import edu.java.client.github.GithubClient;
import edu.java.client.github.GithubSubClient;
import edu.java.client.stackoverflow.StackoverflowClient;
import edu.java.client.stackoverflow.StackoverflowSubClient;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    @Bean
    public GithubClient githubClient(
        ApplicationConfig config,
        List<GithubSubClient> githubSubClients
    ) {
        return new GithubClient(config.api().githubUrl(), githubSubClients);
    }

    @Bean
    public StackoverflowClient stackOverflowClient(
        ApplicationConfig config,
        List<StackoverflowSubClient> stackoverflowSubClients
    ) {
        return new StackoverflowClient(config.api().stackoverflowUrl(), stackoverflowSubClients);
    }
}

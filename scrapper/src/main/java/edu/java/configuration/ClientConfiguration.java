package edu.java.configuration;

import edu.java.client.github.GithubClient;
import edu.java.client.stackoverflow.StackoverflowClient;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    @Bean
    public GithubClient githubClient(
        ApplicationConfig config,
        List<GithubClient.SubClient> githubSubClients
    ) {
        return new GithubClient(config.api().githubUrl(), githubSubClients);
    }

    @Bean
    public StackoverflowClient stackOverflowClient(
        ApplicationConfig config,
        List<StackoverflowClient.SubClient> stackoverflowSubClients
    ) {
        return new StackoverflowClient(config.api().stackoverflowUrl(), stackoverflowSubClients);
    }
}

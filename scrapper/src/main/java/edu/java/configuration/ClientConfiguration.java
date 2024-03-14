package edu.java.configuration;

import edu.java.client.api.github.GithubClient;
import edu.java.client.api.github.GithubLinkHandler;
import edu.java.client.api.stackoverflow.StackoverflowClient;
import edu.java.client.api.stackoverflow.StackoverflowLinkHandler;
import edu.java.client.updates.UpdatesClient;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    @Bean
    public GithubClient githubClient(
        ApplicationConfig config,
        List<GithubLinkHandler> githubLinkHandlers
    ) {
        return new GithubClient(config.api().githubUrl(), githubLinkHandlers);
    }

    @Bean
    public StackoverflowClient stackOverflowClient(
        ApplicationConfig config,
        List<StackoverflowLinkHandler> stackoverflowLinkHandlers
    ) {
        return new StackoverflowClient(config.api().stackoverflowUrl(), stackoverflowLinkHandlers);
    }

    @Bean
    public String botApiUrl(ApplicationConfig config) {
        return config.api().botUrl();
    }

    @Bean
    public UpdatesClient botClient(String botUrl) {
        return new UpdatesClient(botUrl);
    }
}

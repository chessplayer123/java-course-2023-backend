package edu.java.bot.configuration;

import edu.java.bot.client.scrapper.ScrapperClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    @Bean
    public String scrapperApiUrl(ApplicationConfig config) {
        return config.scrapperApiUrl();
    }

    @Bean
    public ScrapperClient botClient(ApplicationConfig config) {
        return new ScrapperClient(config.scrapperApiUrl());
    }
}

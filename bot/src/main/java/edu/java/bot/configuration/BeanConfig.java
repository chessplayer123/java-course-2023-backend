package edu.java.bot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public String telegramBotToken(ApplicationConfig config) {
        return config.telegramToken();
    }
}

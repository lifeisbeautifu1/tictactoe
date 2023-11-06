package ru.tinkoff.tictactoe.config;

import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {
    @Bean("botClient")
    public RestTemplate restTemplate(
        RestTemplateBuilder restTemplateBuilder
    ) {
        return restTemplateBuilder
            .setConnectTimeout(Duration.ofMillis(100))
            .setReadTimeout(Duration.ofSeconds(1))
            .build();
    }
}

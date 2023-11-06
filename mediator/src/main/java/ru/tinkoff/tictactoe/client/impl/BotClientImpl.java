package ru.tinkoff.tictactoe.client.impl;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;

import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import ru.tinkoff.tictactoe.client.BotClient;
import ru.tinkoff.tictactoe.client.BotRequest;
import ru.tinkoff.tictactoe.client.BotResponse;
import ru.tinkoff.tictactoe.client.exception.BotResponseStatusCodeNotOkException;
import ru.tinkoff.tictactoe.client.exception.BotResponseTimeoutException;

@Component
@Slf4j
public class BotClientImpl implements BotClient {

    private final RestTemplate restTemplate;

    private static final String urlPath = "/bot/turn";

    @Autowired
    public BotClientImpl(
        @Qualifier("botClient") RestTemplate restTemplate
    ) {
        this.restTemplate = restTemplate;
    }

    @Override
    public BotResponse makeTurn(URI baseUrl, BotRequest botRequest) {
        String url = String.format("%s%s", baseUrl, urlPath);
        HttpEntity<BotRequest> request = new HttpEntity<>(botRequest);
        log.trace("makeTurn to {}:", url);
        ResponseEntity<BotResponse> response = null;
        try {
            response = restTemplate.exchange(
                url,
                POST,
                request,
                BotResponse.class
            );
        } catch (ResourceAccessException e) {
            throw new BotResponseTimeoutException(e);
        }
        if (response.getStatusCode() != OK) {
            throw new BotResponseStatusCodeNotOkException();
        }
        BotResponse botResponse = response.getBody();
        log.debug("makeTurn from {}: gameField from response {}", url, botResponse.getGameField());

        return botResponse;
    }
}

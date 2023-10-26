package ru.tinkoff.tictactoe.session.exception;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.http.HttpStatusCode;
import ru.tinkoff.tictactoe.ApiException;

public class BotIsAlreadyRegistered extends ApiException {

    public BotIsAlreadyRegistered(String botId) {
        super(format("Bot %s is already registered", botId));
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return BAD_REQUEST;
    }
}

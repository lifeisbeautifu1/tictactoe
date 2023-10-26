package ru.tinkoff.tictactoe.session.exception;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.http.HttpStatusCode;
import ru.tinkoff.tictactoe.ApiException;

public class IncorrectRegistrationDataException extends ApiException {

    public IncorrectRegistrationDataException(String botId, String botUrl) {
        super(format("Bot id %s or bot url %s is incorrect", botId, botUrl));
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return BAD_REQUEST;
    }
}

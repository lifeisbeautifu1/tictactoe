package ru.tinkoff.tictactoe.commands.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import ru.tinkoff.tictactoe.ApiException;

public class InvalidCommandCredentials extends ApiException {

    public InvalidCommandCredentials(String botId) {
        super(String.format("Invalid credentials for bot %s", botId));
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}

package ru.tinkoff.tictactoe.session.exception;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

import java.util.UUID;
import org.springframework.http.HttpStatusCode;
import ru.tinkoff.tictactoe.ApiException;

public class CannotFinishRegistration extends ApiException {

    public CannotFinishRegistration(UUID sessionId, Throwable e) {
        super(format("Cannot finish registration in session %s. Try later", sessionId), e);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return SERVICE_UNAVAILABLE;
    }
}

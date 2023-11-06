package ru.tinkoff.tictactoe.session.exception;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import ru.tinkoff.tictactoe.ApiException;

public class NotParticipantBot extends ApiException {

    public NotParticipantBot(UUID sessionId, String botId) {
        super(String.format("Bot %s is not participant of session %s", botId, sessionId));
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}

package ru.tinkoff.tictactoe.session;

import ru.tinkoff.tictactoe.session.model.CreateSessionRequest;
import ru.tinkoff.tictactoe.session.model.Session;

import java.util.List;
import java.util.UUID;
import ru.tinkoff.tictactoe.session.model.SessionWithLastTurn;

public interface SessionService {
    Session createSession(CreateSessionRequest createSessionRequest);

    Figure registerBotInSession(UUID sessionId, String url, String botId, String password) throws InterruptedException;

    SessionWithLastTurn getSession(UUID sessionId);

    List<Session> getSessionsByIsActive(Boolean isActive);
}

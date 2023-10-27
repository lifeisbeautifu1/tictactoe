package ru.tinkoff.tictactoe.session.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.tictactoe.commands.CommandService;
import ru.tinkoff.tictactoe.session.Figure;
import ru.tinkoff.tictactoe.session.GameService;
import ru.tinkoff.tictactoe.session.SessionRepository;
import ru.tinkoff.tictactoe.session.exception.SessionIsAlreadyFullException;
import ru.tinkoff.tictactoe.session.model.CreateSessionRequest;
import ru.tinkoff.tictactoe.session.model.Session;
import ru.tinkoff.tictactoe.session.model.SessionStatus;
import ru.tinkoff.tictactoe.session.util.lock.LocalLock;

@ExtendWith(MockitoExtension.class)
class SessionServiceImplTest {

    private final SessionRepository sessionRepositoryMock = Mockito.mock(SessionRepository.class);

    private final GameService gameServiceMock = Mockito.mock(GameService.class);
    private final CommandService commandService = Mockito.mock(CommandService.class);

    private final SessionServiceImpl sessionService = new SessionServiceImpl(
        sessionRepositoryMock,
        gameServiceMock,
        new LocalLock(),
        commandService
    );

    //@DisplayName("JUnit test for createSession method")
    @Test
    void whenCreateSession_thenReturnSession() {
        Session session = Session.builder()
            .id(UUID.randomUUID())
            .status(SessionStatus.NEW)
            .createdAt(new Date())
            .updatedAt(new Date())
            .participantBots(Set.of())
            .build();
        final var createSessionRequest = new CreateSessionRequest(Set.of());
        when(sessionRepositoryMock.createSession(createSessionRequest)).thenReturn(session);
        Session returnedSession = sessionService.createSession(createSessionRequest);
        verify(sessionRepositoryMock, times(1)).createSession(createSessionRequest);
        assertThat(returnedSession)
            .isNotNull()
            .isEqualTo(session);
    }

    @Test
    void givenBotIdAndSessionId_whenForTheFirstTimeRegisterBotInSession_thenReturnCrossFigure() {
        UUID sessionId = UUID.randomUUID();
        final var firstBotId = "attacking";
        final var firstBotPassword = "password";
        final var firstBotUrl = String.format("http://%s:%d", "1.1.1.1", 1111);
        Session session = Session.builder()
            .id(sessionId)
            .status(SessionStatus.NEW)
            .createdAt(new Date())
            .updatedAt(new Date())
            .participantBots(Set.of(firstBotId))
            .build();
        when(sessionRepositoryMock.findBySessionId(sessionId)).thenReturn(session);
        Figure figure = sessionService.registerBotInSession(
            sessionId,
            firstBotUrl,
            firstBotId,
            firstBotPassword
        );
        assertThat(figure)
            .isNotNull()
            .isEqualTo(Figure.CROSS);
    }

    @Test
    void givenBotIdAndSessionId_whenForTheSecondTimeRegisterBotInSession_thenReturnZeroFigure()
        throws InterruptedException {
        UUID sessionId = UUID.randomUUID();
        final var firstBotId = "attacking";
        final var firstBotUrl = String.format("http://%s:%d", "1.1.1.1", 1111);
        final var secondBotUrl = String.format("http://%s:%d", "2.2.2.2", 2222);
        final var secondBotId = "defending";
        final var secondBotPassword = "defending";
        Session session = Session.builder()
            .id(sessionId)
            .status(SessionStatus.NEW)
            .attackingBotUrl(firstBotUrl)
            .attackingBotId(firstBotId)
            .createdAt(new Date())
            .updatedAt(new Date())
            .participantBots(Set.of(firstBotId, secondBotId))
            .build();
        when(sessionRepositoryMock.findBySessionId(sessionId)).thenReturn(session);
        Figure figure = sessionService.registerBotInSession(
            sessionId,
            secondBotUrl,
            secondBotId,
            secondBotPassword
        );
        assertThat(figure)
            .isNotNull()
            .isEqualTo(Figure.ZERO);
        verify(gameServiceMock, only()).startGame(sessionId);
    }

    @Test
    void givenBotIdAndSessionId_whenForTheThirdTimeRegisterBotInSession_thenThrowApiException() {
        UUID sessionId = UUID.randomUUID();
        final var firstBotId = "attacking";
        final var firstBotUrl = String.format("http://%s:%d", "1.1.1.1", 1111);
        final var secondBotUrl = String.format("http://%s:%d", "2.2.2.2", 2222);
        final var secondBotId = "defending";
        final var secondBotPassword = "defending";
        Session session = Session.builder()
            .id(sessionId)
            .status(SessionStatus.ONGOING)
            .attackingBotId(firstBotId)
            .attackingBotUrl(firstBotUrl)
            .defendingBotId(secondBotId)
            .defendingBotUrl(secondBotUrl)
            .participantBots(Set.of(firstBotId, secondBotId))
            .createdAt(new Date())
            .updatedAt(new Date())
            .build();
        when(sessionRepositoryMock.findBySessionId(sessionId)).thenReturn(session);
        assertThrows(SessionIsAlreadyFullException.class, () ->
            sessionService.registerBotInSession(sessionId, secondBotUrl, secondBotId, secondBotPassword)
        );
    }
}
package ru.tinkoff.tictactoe.integration.test;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static java.lang.String.format;
import static java.lang.Thread.sleep;
import static java.util.Objects.isNull;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.client.WireMock;
import java.util.Set;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.tinkoff.tictactoe.commands.CommandService;
import ru.tinkoff.tictactoe.commands.model.Command;
import ru.tinkoff.tictactoe.integration.IntegrationSettings;
import ru.tinkoff.tictactoe.session.GameService;
import ru.tinkoff.tictactoe.session.SessionRepository;
import ru.tinkoff.tictactoe.session.SessionService;
import ru.tinkoff.tictactoe.session.model.CreateSessionRequest;
import ru.tinkoff.tictactoe.session.model.Session;
import ru.tinkoff.tictactoe.session.model.SessionWithLastTurn;

@Disabled
class GameServiceTest extends IntegrationSettings {

    @Autowired
    private CommandService commandService;
    @Autowired
    private GameService gameService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionService sessionService;

    @Test
    void givenSessionWithTwoBots_whenStartGame_thenNewSessionHas10Turns()
        throws InterruptedException {
        final var attackingBotId = "attacking";
        final var defendingBotId = "defending";
        String firstBotIp = "1.1.1.1";
        int firstBotPort = 1111;
        String secondBotIp = "2.2.2.2";
        int secondBotPort = 2222;

        wireMockServer.stubFor(WireMock.post("/bot/turn")
                                   .withHost(equalTo(firstBotIp))
                                   .withPort(firstBotPort)
                                   .withRequestBody(equalToJson("{\"game_field\": \"" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "\"}"))
                                   .willReturn(WireMock.aResponse()
                                                   .withStatus(200)
                                                   .withHeader("Content-Type", "application/json")
                                                   .withBody("{\"game_field\": \"" +
                                                                 "_x_________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "\"}")));

        wireMockServer.stubFor(WireMock.post("/bot/turn")
                                   .withHost(equalTo(secondBotIp))
                                   .withPort(secondBotPort)
                                   .withRequestBody(equalToJson("{\"game_field\": \"" +
                                                                    "_x_________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "\"}"))
                                   .willReturn(WireMock.aResponse()
                                                   .withStatus(200)
                                                   .withHeader("Content-Type", "application/json")
                                                   .withBody("{\"game_field\": \"" +
                                                                 "_xo________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "\"}")));
        wireMockServer.stubFor(WireMock.post("/bot/turn")
                                   .withHost(equalTo(firstBotIp))
                                   .withPort(firstBotPort)
                                   .withRequestBody(equalToJson("{\"game_field\": \"" +
                                                                    "_xo________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "\"}"))
                                   .willReturn(WireMock.aResponse()
                                                   .withStatus(200)
                                                   .withHeader("Content-Type", "application/json")
                                                   .withBody("{\"game_field\": \"" +
                                                                 "_xo________________" +
                                                                 "_x_________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "\"}")));
        wireMockServer.stubFor(WireMock.post("/bot/turn")
                                   .withHost(equalTo(secondBotIp))
                                   .withPort(secondBotPort)
                                   .withRequestBody(equalToJson("{\"game_field\": \"" +
                                                                    "_xo________________" +
                                                                    "_x_________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "\"}"))
                                   .willReturn(WireMock.aResponse()
                                                   .withStatus(200)
                                                   .withHeader("Content-Type", "application/json")
                                                   .withBody("{\"game_field\": \"" +
                                                                 "_xo________________" +
                                                                 "_xo________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "\"}")));
        wireMockServer.stubFor(WireMock.post("/bot/turn")
                                   .withHost(equalTo(firstBotIp))
                                   .withPort(firstBotPort)
                                   .withRequestBody(equalToJson("{\"game_field\": \"" +
                                                                    "_xo________________" +
                                                                    "_xo________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "\"}"))
                                   .willReturn(WireMock.aResponse()
                                                   .withStatus(200)
                                                   .withHeader("Content-Type", "application/json")
                                                   .withBody("{\"game_field\": \"" +
                                                                 "_xo________________" +
                                                                 "_xo________________" +
                                                                 "_x_________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "\"}")));
        wireMockServer.stubFor(WireMock.post("/bot/turn")
                                   .withHost(equalTo(secondBotIp))
                                   .withPort(secondBotPort)
                                   .withRequestBody(equalToJson("{\"game_field\": \"" +
                                                                    "_xo________________" +
                                                                    "_xo________________" +
                                                                    "_x_________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "\"}"))
                                   .willReturn(WireMock.aResponse()
                                                   .withStatus(200)
                                                   .withHeader("Content-Type", "application/json")
                                                   .withBody("{\"game_field\": \"" +
                                                                 "_xo________________" +
                                                                 "_xo________________" +
                                                                 "_xo________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "\"}")));
        wireMockServer.stubFor(WireMock.post("/bot/turn")
                                   .withHost(equalTo(firstBotIp))
                                   .withPort(firstBotPort)
                                   .withRequestBody(equalToJson("{\"game_field\": \"" +
                                                                    "_xo________________" +
                                                                    "_xo________________" +
                                                                    "_xo________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "\"}"))
                                   .willReturn(WireMock.aResponse()
                                                   .withStatus(200)
                                                   .withHeader("Content-Type", "application/json")
                                                   .withBody("{\"game_field\": \"" +
                                                                 "_xo________________" +
                                                                 "_xo________________" +
                                                                 "_xo________________" +
                                                                 "_x_________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "\"}")));
        wireMockServer.stubFor(WireMock.post("/bot/turn")
                                   .withHost(equalTo(secondBotIp))
                                   .withPort(secondBotPort)
                                   .withRequestBody(equalToJson("{\"game_field\": \"" +
                                                                    "_xo________________" +
                                                                    "_xo________________" +
                                                                    "_xo________________" +
                                                                    "_x_________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "\"}"))
                                   .willReturn(WireMock.aResponse()
                                                   .withStatus(200)
                                                   .withHeader("Content-Type", "application/json")
                                                   .withBody("{\"game_field\": \"" +
                                                                 "_xo________________" +
                                                                 "_xo________________" +
                                                                 "_xo________________" +
                                                                 "_xo________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "\"}")));
        wireMockServer.stubFor(WireMock.post("/bot/turn")
                                   .withHost(equalTo(firstBotIp))
                                   .withPort(firstBotPort)
                                   .withRequestBody(equalToJson("{\"game_field\": \"" +
                                                                    "_xo________________" +
                                                                    "_xo________________" +
                                                                    "_xo________________" +
                                                                    "_xo________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "\"}"))
                                   .willReturn(WireMock.aResponse()
                                                   .withStatus(200)
                                                   .withHeader("Content-Type", "application/json")
                                                   .withBody("{\"game_field\": \"" +
                                                                 "_xo________________" +
                                                                 "_xo________________" +
                                                                 "_xo________________" +
                                                                 "_xo________________" +
                                                                 "_x_________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "\"}")));
        commandService.saveCommand(new Command(
            attackingBotId,
            attackingBotId
        ));
        commandService.saveCommand(new Command(
            defendingBotId,
            defendingBotId
        ));
        Session session = sessionService.createSession(new CreateSessionRequest(Set.of(
            attackingBotId,
            defendingBotId
        )));
        sessionRepository.setAttackingBot(
            session.id(),
            format("http://%s:%d", firstBotIp, firstBotPort),
            attackingBotId
        );
        sessionRepository.setDefendingBot(
            session.id(),
            format("http://%s:%d", secondBotIp, secondBotPort),
            defendingBotId
        );
        gameService.startGame(session.id());
        var finishedSession = waitGameFinished(session);
        assertThat(finishedSession.winBot()).isEqualTo(attackingBotId);
    }

    @Test
    void givenSessionWithOneTimeoutBot_whenStartGame_thenOnlyCorrectBotHasCorrectTurns()
        throws InterruptedException {
        final var attackingBotId = "attacking1";
        final var defendingBotId = "defending1";
        String firstBotIp = "1.1.1.1";
        int firstBotPort = 1111;
        String secondBotIp = "2.2.2.2";
        int secondBotPort = 2222;

        wireMockServer.stubFor(WireMock.post("/bot/turn")
                                   .withHost(equalTo(firstBotIp))
                                   .withPort(firstBotPort)
                                   .withRequestBody(equalToJson("{\"game_field\": \"" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "___________________" +
                                                                    "\"}"))
                                   .willReturn(WireMock.aResponse()
                                                   .withStatus(200)
                                                   .withFixedDelay(2000)
                                                   .withHeader("Content-Type", "application/json")
                                                   .withBody("{\"game_field\": \"" +
                                                                 "_x_________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "___________________" +
                                                                 "\"}")));
        commandService.saveCommand(new Command(
            attackingBotId,
            attackingBotId
        ));
        commandService.saveCommand(new Command(
            defendingBotId,
            defendingBotId
        ));
        Session session = sessionService.createSession(new CreateSessionRequest(Set.of(
            attackingBotId,
            defendingBotId
        )));
        sessionRepository.setAttackingBot(
            session.id(),
            format("http://%s:%d", firstBotIp, firstBotPort),
            attackingBotId
        );
        sessionRepository.setDefendingBot(
            session.id(),
            format("http://%s:%d", secondBotIp, secondBotPort),
            defendingBotId
        );
        gameService.startGame(session.id());
        var finishedSession = waitGameFinished(session);
        assertThat(finishedSession.winBot()).isEqualTo(defendingBotId);
    }

    private SessionWithLastTurn waitGameFinished(Session session) throws InterruptedException {
        SessionWithLastTurn finishedSession;
        do {
            finishedSession = sessionService.getSession(session.id());
            sleep(500);
        } while (isNull(finishedSession.winBot()));
        return finishedSession;
    }
}

package ru.tinkoff.tictactoe.session.model;

import java.util.Set;

public record CreateSessionRequest(
   Set<String> participantBots
) {}

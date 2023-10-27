package ru.tinkoff.tictactoe.session.controller;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

@JsonNaming(SnakeCaseStrategy.class)
record CreateSessionRequestDto(
    @NotEmpty
    Set<String> participantBots
) {
}

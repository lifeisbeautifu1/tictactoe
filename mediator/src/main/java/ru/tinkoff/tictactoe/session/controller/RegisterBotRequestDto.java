package ru.tinkoff.tictactoe.session.controller;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(SnakeCaseStrategy.class)
record RegisterBotRequestDto(
    @NotBlank
    String botUrl,
    @NotBlank
    String botId,
    @NotBlank
    String password
) {}

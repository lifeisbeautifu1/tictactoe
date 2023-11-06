package ru.tinkoff.tictactoe.session.controller;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
@JsonNaming(SnakeCaseStrategy.class)
record CreateSessionResponseDto(
    UUID sessionId,
    LocalDateTime createdAt,
    String status
) {}

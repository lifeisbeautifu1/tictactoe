package ru.tinkoff.tictactoe.config;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.tinkoff.tictactoe.ApiException;

@Slf4j
@ControllerAdvice
public class ExceptionHandlingConfig {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleApiException(
        ApiException exception
    ) {
        log.error("Api exception", exception);
        return ResponseEntity.status(exception.getStatusCode())
            .body(new CommonErrorDto(exception.getMessage()));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleOtherException(Throwable e) {
        log.error("Unknown exception", e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
            .body(new CommonErrorDto("Unknown exception"));
    }

}

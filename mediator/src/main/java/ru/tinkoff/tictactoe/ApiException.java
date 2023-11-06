package ru.tinkoff.tictactoe;

import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

public abstract class ApiException extends RuntimeException implements ErrorResponse {

    public ApiException(String message) {
        this(message, null);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public ProblemDetail getBody() {
        return ProblemDetail.forStatusAndDetail(
            getStatusCode(),
            getMessage()
        );
    }
}

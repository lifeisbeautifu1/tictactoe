package ru.tinkoff.tictactoe.client.exception;

public class BotResponseTimeoutException extends RuntimeException {

    public BotResponseTimeoutException(Throwable cause) {
        super("Бот не успел ответить за отведенное время¬", cause);
    }
}

package ru.tinkoff.tictactoe.session.util.lock;

public class LockException extends RuntimeException {

    public LockException() {
    }

    public LockException(Throwable cause) {
        super(cause);
    }
}

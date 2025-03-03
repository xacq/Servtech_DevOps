package dev.magadiflo.app.exception;

public class NotFoundEntity extends RuntimeException {
    public NotFoundEntity(String message) {
        super(message);
    }
}

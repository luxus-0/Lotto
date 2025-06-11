package pl.lotto.application.notification.exceptions;

public class BodyEmailNotFoundException extends RuntimeException {
    public BodyEmailNotFoundException(String message) {
        super(message);
    }
}

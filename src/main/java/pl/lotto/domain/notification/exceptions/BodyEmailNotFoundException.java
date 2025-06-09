package pl.lotto.domain.notification.exceptions;

public class BodyEmailNotFoundException extends RuntimeException {
    public BodyEmailNotFoundException(String message) {
        super(message);
    }
}

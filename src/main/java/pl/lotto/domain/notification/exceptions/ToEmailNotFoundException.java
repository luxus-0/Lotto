package pl.lotto.domain.notification.exceptions;

public class ToEmailNotFoundException extends RuntimeException {
    public ToEmailNotFoundException(String message) {
        super(message);
    }
}

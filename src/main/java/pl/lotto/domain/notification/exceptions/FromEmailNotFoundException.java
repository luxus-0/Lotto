package pl.lotto.domain.notification.exceptions;

public class FromEmailNotFoundException extends RuntimeException {
    public FromEmailNotFoundException(String message) {
        super(message);
    }
}

package pl.lotto.infrastructure.email.exceptions;

public class ToEmailNotFoundException extends RuntimeException {
    public ToEmailNotFoundException(String message) {
        super(message);
    }
}

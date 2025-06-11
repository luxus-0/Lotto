package pl.lotto.infrastructure.email.exceptions;

public class BodyEmailNotFoundException extends RuntimeException {
    public BodyEmailNotFoundException(String message) {
        super(message);
    }
}

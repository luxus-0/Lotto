package pl.lotto.infrastructure.email.exceptions;

public class FromEmailNotFoundException extends RuntimeException {
    public FromEmailNotFoundException(String message) {
        super(message);
    }
}

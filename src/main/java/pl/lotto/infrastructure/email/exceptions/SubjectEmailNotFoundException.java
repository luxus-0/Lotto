package pl.lotto.infrastructure.email.exceptions;

public class SubjectEmailNotFoundException extends RuntimeException {
    public SubjectEmailNotFoundException(String message) {
        super(message);
    }
}

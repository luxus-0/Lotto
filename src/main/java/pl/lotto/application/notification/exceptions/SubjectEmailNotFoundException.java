package pl.lotto.application.notification.exceptions;

public class SubjectEmailNotFoundException extends RuntimeException {
    public SubjectEmailNotFoundException(String message) {
        super(message);
    }
}

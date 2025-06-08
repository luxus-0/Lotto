package pl.lotto.domain.notification.exceptions;

public class SubjectEmailNotFoundException extends RuntimeException{
    public SubjectEmailNotFoundException(String message) {
        super(message);
    }
}

package pl.lotto.infrastructure.email.exceptions;

public class MessageEmailNotFoundException extends RuntimeException {
    public MessageEmailNotFoundException(String message) {
        super(message);
    }
}

package pl.lotto.application.notification.exceptions;

public class MessageEmailNotFoundException extends RuntimeException {
    public MessageEmailNotFoundException(String message) {
        super(message);
    }
}

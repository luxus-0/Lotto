package pl.lotto.application.notification.exceptions;

public class EmailApiKeyNotFoundException extends RuntimeException {
    public EmailApiKeyNotFoundException(String message) {
        super(message);
    }
}

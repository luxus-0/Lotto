package pl.lotto.infrastructure.email.exceptions;

public class EmailApiKeyNotFoundException extends RuntimeException {
    public EmailApiKeyNotFoundException(String message) {
        super(message);
    }
}

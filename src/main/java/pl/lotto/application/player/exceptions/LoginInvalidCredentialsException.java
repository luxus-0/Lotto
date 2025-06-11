package pl.lotto.application.player.exceptions;

class LoginInvalidCredentialsException extends RuntimeException {
    public LoginInvalidCredentialsException(String message) {
        super(message);
    }
}

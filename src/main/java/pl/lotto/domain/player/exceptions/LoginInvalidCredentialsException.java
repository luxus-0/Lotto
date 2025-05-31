package pl.lotto.domain.player.exceptions;

class LoginInvalidCredentialsException extends RuntimeException {
    public LoginInvalidCredentialsException(String message) {
        super(message);
    }
}

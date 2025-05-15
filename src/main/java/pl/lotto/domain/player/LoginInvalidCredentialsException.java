package pl.lotto.domain.player;

class LoginInvalidCredentialsException extends RuntimeException {
    public LoginInvalidCredentialsException(String message) {
        super(message);
    }
}

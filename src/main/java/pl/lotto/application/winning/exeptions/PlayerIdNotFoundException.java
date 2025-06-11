package pl.lotto.application.winning.exeptions;

public class PlayerIdNotFoundException extends RuntimeException {
    public PlayerIdNotFoundException(String message) {
        super(message);
    }
}

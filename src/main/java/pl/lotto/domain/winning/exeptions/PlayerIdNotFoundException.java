package pl.lotto.domain.winning.exeptions;

public class PlayerIdNotFoundException extends RuntimeException {
    public PlayerIdNotFoundException(String message) {
        super(message);
    }
}

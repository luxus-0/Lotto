package pl.lotto.domain.winning.exeptions;

public class PlayerNumbersNotFoundExceptions extends RuntimeException {
    public PlayerNumbersNotFoundExceptions(String message) {
        super(message);
    }
}

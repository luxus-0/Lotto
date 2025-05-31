package pl.lotto.domain.winning.exeptions;

public class WinningDateNotFoundException extends RuntimeException {
    public WinningDateNotFoundException(String message) {
        super(message);
    }
}

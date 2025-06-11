package pl.lotto.application.winning.exeptions;

public class WinningDateNotFoundException extends RuntimeException {
    public WinningDateNotFoundException(String message) {
        super(message);
    }
}

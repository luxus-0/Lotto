package pl.lotto.domain.draw;

public class InvalidNumberRangeException extends RuntimeException {
    public InvalidNumberRangeException(String message) {
        super(message);
    }
}

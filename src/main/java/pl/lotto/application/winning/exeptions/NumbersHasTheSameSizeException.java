package pl.lotto.application.winning.exeptions;

public class NumbersHasTheSameSizeException extends RuntimeException {
    public NumbersHasTheSameSizeException(String message) {
        super(message);
    }
}

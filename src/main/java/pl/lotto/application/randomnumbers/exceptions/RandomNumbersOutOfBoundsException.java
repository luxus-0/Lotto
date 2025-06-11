package pl.lotto.application.randomnumbers.exceptions;

public class RandomNumbersOutOfBoundsException extends RuntimeException {
    public RandomNumbersOutOfBoundsException(String message) {
        super(message);
    }
}

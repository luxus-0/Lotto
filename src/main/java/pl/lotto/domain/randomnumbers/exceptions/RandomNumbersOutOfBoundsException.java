package pl.lotto.domain.randomnumbers.exceptions;

public class RandomNumbersOutOfBoundsException extends RuntimeException {
    public RandomNumbersOutOfBoundsException(String message) {
        super(message);
    }
}

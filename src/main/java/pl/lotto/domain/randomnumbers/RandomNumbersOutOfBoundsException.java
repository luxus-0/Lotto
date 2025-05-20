package pl.lotto.domain.randomnumbers;

public class RandomNumbersOutOfBoundsException extends RuntimeException {
    public RandomNumbersOutOfBoundsException(String message) {
        super(message);
    }
}

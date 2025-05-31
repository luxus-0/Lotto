package pl.lotto.domain.randomnumbers.exceptions;

public class RandomNumbersNotFoundException extends RuntimeException {
    public RandomNumbersNotFoundException(String message) {
        super(message);
    }
}

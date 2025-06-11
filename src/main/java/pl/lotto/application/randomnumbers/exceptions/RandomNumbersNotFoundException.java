package pl.lotto.application.randomnumbers.exceptions;

public class RandomNumbersNotFoundException extends RuntimeException {
    public RandomNumbersNotFoundException(String message) {
        super(message);
    }
}

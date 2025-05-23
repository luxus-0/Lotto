package pl.lotto.domain.randomnumbers;

public class RandomNumbersNotFoundException extends RuntimeException {
    public RandomNumbersNotFoundException(String message) {
        super(message);
    }
}

package pl.lotto.domain.player;

public class PlayerProfileNotFoundException extends RuntimeException{
    public PlayerProfileNotFoundException(String message) {
        super(message);
    }
}

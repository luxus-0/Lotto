package pl.lotto.domain.player;

class PlayerProfileNotFoundException extends RuntimeException{
    public PlayerProfileNotFoundException(String message) {
        super(message);
    }
}

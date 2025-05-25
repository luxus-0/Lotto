package pl.lotto.domain.player;

class PlayerAlreadyExistsException extends RuntimeException {

    public PlayerAlreadyExistsException(String message, String name, String surname) {
        super(message);
    }

    public PlayerAlreadyExistsException(String message, String email) {
        super(message);
    }
}

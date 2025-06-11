package pl.lotto.application.player.exceptions;

public class PlayerAlreadyExistsException extends RuntimeException {
    public PlayerAlreadyExistsException(String message, Object... args) {
        super(String.format(message.replace("{}", "%s"), args));
    }
}

package pl.lotto.domain.ticket;

public class TicketNumbersOutOfBoundsException extends RuntimeException {
    public TicketNumbersOutOfBoundsException(String message) {
        super(message);
    }
}

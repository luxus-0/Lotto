package pl.lotto.domain.ticket.exceptions;

public class TicketNumbersOutOfBoundsException extends RuntimeException {
    public TicketNumbersOutOfBoundsException(String message) {
        super(message);
    }
}

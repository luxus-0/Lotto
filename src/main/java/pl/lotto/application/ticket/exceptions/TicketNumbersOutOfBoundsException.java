package pl.lotto.application.ticket.exceptions;

public class TicketNumbersOutOfBoundsException extends RuntimeException {
    public TicketNumbersOutOfBoundsException(String message) {
        super(message);
    }
}

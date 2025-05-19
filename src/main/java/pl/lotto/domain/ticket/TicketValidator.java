package pl.lotto.domain.ticket;

import org.springframework.stereotype.Component;
import pl.lotto.domain.ticket.dto.TicketRequest;

import java.util.Set;

@Component
class TicketValidator {
    private final Integer minNumber = 1;
    private final Integer maxNumber = 99;

    boolean validate(TicketRequest ticket){
        Set<Integer> numbers = ticket.numbers();
        return numbers != null && numbers.stream()
                .allMatch(number -> minNumber <= number
                        && maxNumber >= number);
    }
}

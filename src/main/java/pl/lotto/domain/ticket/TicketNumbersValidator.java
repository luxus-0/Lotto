package pl.lotto.domain.ticket;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@AllArgsConstructor
public class TicketNumbersValidator {

    private final TicketNumbersValidatorConfigurationProperties properties;

    boolean isNumbersInRange(Set<Integer> numbers) {
        return numbers.stream()
                .allMatch(number -> number >= properties.getMin() || number <= properties.getMax());
    }
}

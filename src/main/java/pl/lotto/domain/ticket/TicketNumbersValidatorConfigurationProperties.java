package pl.lotto.domain.ticket;

import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ticket.numbers")
@Builder
@Getter
public class TicketNumbersValidatorConfigurationProperties {
    private Integer min;
    private Integer max;
}

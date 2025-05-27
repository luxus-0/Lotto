package pl.lotto.domain.ticket;

import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ticket.winNumbers")
@Builder
@Getter
public class TicketNumbersValidatorConfigurationProperties {
    private Integer min;
    private Integer max;
}

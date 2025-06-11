package pl.lotto.application.ticket;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ticket.numbers")
@Builder
@Getter
public class TicketNumbersValidatorConfigurationProperties {
    @Min(1)
    private Integer min;
    @Max(99)
    private Integer max;
}

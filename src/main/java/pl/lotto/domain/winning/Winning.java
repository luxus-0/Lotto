package pl.lotto.domain.winning;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "winnings")
@Builder
@Getter
public class Winning {

    @Id
    private UUID id;
    private UUID playerId;
    private Integer hits;
    private BigDecimal price;
    private LocalDateTime drawDate;
}

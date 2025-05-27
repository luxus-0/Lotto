package pl.lotto.domain.drawdatetime;

import java.time.LocalDateTime;

public interface DrawDateTimeQueryService {
    LocalDateTime generateDrawDateTime();
}

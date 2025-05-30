package pl.lotto.domain.player;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lotto.domain.drawdatetime.DrawDateTimeQueryServiceImpl;
import pl.lotto.domain.player.dto.PlayerRequest;
import pl.lotto.domain.player.dto.PlayerStatistics;
import pl.lotto.domain.randomnumbers.RandomNumbersGeneratorQueryServiceImpl;
import pl.lotto.domain.ticket.TicketQueryServiceImpl;

@Service
@AllArgsConstructor
public class PlayerStatisticsQueryServiceImpl implements PlayerStatisticsQueryService {
    private final RandomNumbersGeneratorQueryServiceImpl randomNumbersQueryService;
    private final DrawDateTimeQueryServiceImpl drawDateTimeQueryService;
    private final TicketQueryServiceImpl ticketQueryService;

    @Override
    public PlayerStatistics generateStats(PlayerRequest playerRequest) {
        return PlayerStatistics.builder()
                .build();
    }
}

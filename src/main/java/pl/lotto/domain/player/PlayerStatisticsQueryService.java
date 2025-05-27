package pl.lotto.domain.player;

import pl.lotto.domain.player.dto.PlayerRequest;
import pl.lotto.domain.player.dto.PlayerStatistics;

public interface PlayerStatisticsQueryService {

    PlayerStatistics generateStats(PlayerRequest playerRequest);
}

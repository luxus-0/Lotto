package pl.lotto.domain.player;

import pl.lotto.domain.player.dto.PlayerRequest;
import pl.lotto.domain.player.dto.PlayerResponse;

import java.util.Set;
import java.util.UUID;

public interface PlayerQueryService {
    PlayerResponse registerPlayer(PlayerRequest playerRequest);

    PlayerResponse findPlayer(UUID playerId);

    Set<PlayerResponse> findPlayers();

    PlayerResponse updatePlayer(UUID playerId, PlayerRequest playerRequest);

    void removePlayer(UUID playerId);
}

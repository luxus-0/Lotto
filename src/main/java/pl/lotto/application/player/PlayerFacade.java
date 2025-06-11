package pl.lotto.application.player;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lotto.application.player.dto.PlayerRequest;
import pl.lotto.application.player.dto.PlayerResponse;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerFacade {
    private final PlayerService playerService;

    PlayerResponse register(PlayerRequest player) {
        return playerService.registerPlayer(player);
    }

    public PlayerResponse updatePlayer(UUID playerId) {
        return playerService.updatePlayer(playerId);
    }

    PlayerResponse find(UUID playerId) {
        return playerService.findPlayer(playerId);
    }

    Set<PlayerResponse> findAll() {
        return playerService.findPlayers();
    }

    void delete(UUID playerId) {
        playerService.removePlayer(playerId);
    }
}

package pl.lotto.domain.player;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.lotto.domain.player.dto.PlayerRequest;
import pl.lotto.domain.player.dto.PlayerResponse;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerFacade {
    private final PlayerService playerService;

    public PlayerResponse register(PlayerRequest player) {
        return playerService.registerPlayer(player);
    }

    public PlayerResponse update(PlayerRequest player) {
        return playerService.updatePlayer(player.id(), player);
    }

    public PlayerResponse find(UUID playerId) {
        return playerService.findPlayer(playerId);
    }

    public Set<PlayerResponse> findAll() {
        return playerService.findPlayers();
    }

    public void delete(UUID playerId) {
        playerService.removePlayer(playerId);
    }
}

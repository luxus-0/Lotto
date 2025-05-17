package pl.lotto.domain.player;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lotto.domain.player.dto.PlayerLoginDto;
import pl.lotto.domain.player.dto.PlayerLoginRequest;
import pl.lotto.domain.player.dto.PlayerRegistrationRequest;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PlayerFacade {

    private final PlayerService playerService;

    public void register(PlayerRegistrationRequest request) {
        playerService.registerPlayer(request);
    }

    public PlayerLoginDto login(PlayerLoginRequest request) {
        return playerService.loginPlayer(request);
    }

    public void activatePlayer(UUID playerId) {
        playerService.updatePlayerStatus(playerId, true);
    }

    public void deactivatePlayer(UUID playerId) {
        playerService.updatePlayerStatus(playerId, false);
    }
}

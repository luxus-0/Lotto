package pl.lotto.domain.player;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.lotto.domain.player.dto.PlayerProfileDto;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PlayerProfileFacade {

    private final PlayerProfileService playerProfileService;

    public PlayerProfile getProfileByPlayerId(UUID playerId) {
        return playerProfileService.findByPlayerProfileById(playerId);
    }

    public PlayerProfile updateProfile(UUID playerId, PlayerProfileDto dto) {
        return playerProfileService.updateProfile(playerId, dto);
    }

    public void deleteProfile(UUID playerId) {
        playerProfileService.deleteProfile(playerId);
    }
}
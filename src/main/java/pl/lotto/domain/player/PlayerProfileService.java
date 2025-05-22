package pl.lotto.domain.player;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.lotto.domain.player.dto.PlayerProfileDto;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class PlayerProfileService {

    private final PlayerProfileRepository profileRepository;

    public PlayerProfile findByPlayerProfileById(UUID playerId) {
        return profileRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new PlayerProfileNotFoundException("Player profile not found"));
    }

    public PlayerProfile updateProfile(UUID playerId, PlayerProfileDto playerProfile) {
        PlayerProfile profile = profileRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new PlayerProfileNotFoundException("Profile not found"));

        PlayerProfile updated = PlayerProfile.builder()
                .totalGamesPlayed(playerProfile.totalGamesPlayed())
                .totalWins(playerProfile.totalWins())
                .description(playerProfile.description())
                .imageUrl(playerProfile.imageUrl())
                .createdAt(profile.getCreatedAt())
                .build();

        PlayerProfile saved = profileRepository.save(updated);
        log.info("Updated profile for playerId {}: {}", playerId, saved);
        return saved;
    }

    public void deleteProfile(UUID playerId) {
        profileRepository.findByPlayerId(playerId).ifPresent(profile -> {
            profileRepository.delete(profile);
            log.info("Deleted profile for playerId: {}", playerId);
        });
    }
}
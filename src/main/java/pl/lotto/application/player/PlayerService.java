package pl.lotto.application.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.lotto.application.player.dto.PlayerRequest;
import pl.lotto.application.player.dto.PlayerResponse;
import pl.lotto.application.player.exceptions.PlayerNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static pl.lotto.application.player.PlayerStatus.ACTIVE;
import static pl.lotto.application.player.PlayerStatus.INACTIVE;


@Service
@RequiredArgsConstructor
@Log4j2
class PlayerService {
    private final static String PLAYER_NOT_FOUND = "Player not found";
    private final PlayerRepository playerRepository;
    private final ObjectMapper objectMapper;

    private static PlayerResponse getPlayerStatusInactive(PlayerRequest playerRequest) {
        return PlayerResponse.builder()
                .isCreated(false)
                .status(INACTIVE)
                .name(playerRequest.name())
                .build();
    }

    private static PlayerResponse getPlayer(Player playerSaved) {
        return PlayerResponse.builder()
                .name(playerSaved.name())
                .status(playerSaved.status())
                .isCreated(true)
                .build();
    }

    private static Player getPlayerStatusActive(PlayerRequest playerRequest) {
        return Player.builder()
                .id(UUID.randomUUID())
                .name(playerRequest.name())
                .email(playerRequest.email())
                .status(ACTIVE)
                .createdAt(now())
                .build();
    }

    public PlayerResponse registerPlayer(PlayerRequest playerRequest) {
        boolean existPlayerById = playerRepository.existsPlayerById(playerRequest.id());
        boolean existPlayerByEmail = playerRepository.existsPlayerByEmail(playerRequest.email());
        if (!existPlayerById && !existPlayerByEmail) {
            Player player = getPlayerStatusActive(playerRequest);
            Player playerSaved = playerRepository.save(player);

            return getPlayer(playerSaved);
        }
        return getPlayerStatusInactive(playerRequest);
    }

    public PlayerResponse findPlayer(UUID playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(PLAYER_NOT_FOUND));

        return objectMapper.convertValue(player, PlayerResponse.class);
    }


    public Set<PlayerResponse> findPlayers() {
        List<Player> players = playerRepository.findAll();
        players.stream().findAny().orElseThrow(() -> new PlayerNotFoundException(PLAYER_NOT_FOUND));
        return players.stream()
                .map(player -> objectMapper.convertValue(player, PlayerResponse.class))
                .collect(Collectors.toSet());
    }

    public PlayerResponse updatePlayer(UUID playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(PLAYER_NOT_FOUND + " with id: " + playerId));

        Player updatedPlayer = Player.builder()
                .id(playerId)
                .name(player.name())
                .surname(player.surname())
                .email(player.email())
                .status(player.status())
                .createdAt(player.createdAt())
                .build();

        Player savedPlayer = playerRepository.save(updatedPlayer);
        log.info("Player with id {} updated: {}", playerId, savedPlayer);
        return objectMapper.convertValue(savedPlayer, PlayerResponse.class);
    }

    public void removePlayer(UUID playerId) {
        Player playerToDelete = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(PLAYER_NOT_FOUND + " with id: " + playerId));
        playerRepository.delete(playerToDelete);
        log.info("Player with id {} removed", playerId);
    }
}

package pl.lotto.domain.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.lotto.domain.player.dto.PlayerRequest;
import pl.lotto.domain.player.dto.PlayerResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.lotto.domain.player.PlayerRegisterStatus.REGISTER_FAIL;
import static pl.lotto.domain.player.PlayerRegisterStatus.REGISTER_SUCCESS;

@Service
@RequiredArgsConstructor
@Log4j2
class PlayerService {
    private final PlayerRepository playerRepository;
    private final ObjectMapper objectMapper;
    private final static String PLAYER_NOT_FOUND = "Player not found";

    PlayerResponse registerPlayer(PlayerRequest playerRequest) {
        validatePlayerDoesNotExist(playerRequest);
        Player player = getPlayer(playerRequest);
        Player playerSaved = playerRepository.save(player);
        log.info("Player saved: {}", playerSaved);

        PlayerResponse response = toPlayer(playerRequest, playerSaved);
        if (response == null) {
            return getRegistrationPlayerFailure();
        }
        return response;
    }

    private void validatePlayerDoesNotExist(PlayerRequest playerRequest) {
        String name = playerRequest.name();
        String surname = playerRequest.surname();
        String email = playerRequest.email();
        if (playerRepository.existsByNameAndSurname(name, surname)) {
            throw new PlayerAlreadyExistsException("Player {} {} already exists", name, surname);
        }
        if (playerRepository.existsByEmail(playerRequest.email())) {
            throw new PlayerAlreadyExistsException("Player email {} already exists", email);
        }
    }

    private static PlayerResponse getRegistrationPlayerFailure() {
        return PlayerResponse.builder()
                .isCreated(false)
                .result(REGISTER_FAIL.name())
                .status(PlayerStatus.INACTIVE)
                .build();
    }

    private static PlayerResponse toPlayer(PlayerRequest playerRequest, Player playerSaved) {
        return PlayerResponse.builder()
                .id(playerSaved.id())
                .name(playerRequest.name())
                .surname(playerRequest.surname())
                .isCreated(true)
                .result(REGISTER_SUCCESS.name())
                .status(PlayerStatus.ACTIVE)
                .build();
    }

    private static Player getPlayer(PlayerRequest playerRequest) {
        return Player.builder()
                .id(UUID.randomUUID())
                .name(playerRequest.name())
                .surname(playerRequest.surname())
                .email(playerRequest.email())
                .createdAt(LocalDateTime.now())
                .build();
    }

    PlayerResponse findPlayer(UUID playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(PLAYER_NOT_FOUND));

        return objectMapper.convertValue(player, PlayerResponse.class);
    }

    Set<PlayerResponse> findPlayers() {
        List<Player> players = playerRepository.findAll();
        if (players.isEmpty()) {
            throw new PlayerNotFoundException(PLAYER_NOT_FOUND);
        }
        return players.stream().map(pl -> objectMapper.convertValue(pl, PlayerResponse.class))
                .collect(Collectors.toSet());
    }

    PlayerResponse updatePlayer(UUID playerId, PlayerRequest playerRequest) {
        Player existingPlayer = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(PLAYER_NOT_FOUND + " with id: " + playerId));

        Player updatedPlayer = new Player(
                existingPlayer.id(),
                playerRequest.name(),
                playerRequest.surname(),
                playerRequest.email(),
                existingPlayer.createdAt(),
                existingPlayer.status()
        );

        Player savedPlayer = playerRepository.save(updatedPlayer);
        log.info("Player with id {} updated: {}", playerId, savedPlayer);
        return objectMapper.convertValue(savedPlayer, PlayerResponse.class);
    }

    void removePlayer(UUID playerId) {
        Player playerToDelete = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(PLAYER_NOT_FOUND + " with id: " + playerId));
        playerRepository.delete(playerToDelete);
        log.info("Player with id {} removed", playerId);
    }
}

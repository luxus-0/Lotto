package pl.lotto.domain.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pl.lotto.domain.player.dto.PlayerRequest;
import pl.lotto.domain.player.dto.PlayerResponse;
import pl.lotto.domain.player.exceptions.PlayerAlreadyExistsException;
import pl.lotto.domain.player.exceptions.PlayerNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.lotto.domain.player.PlayerRegisterStatus.REGISTER_SUCCESS;
import static pl.lotto.domain.player.PlayerStatus.ACTIVE;


@Service
@RequiredArgsConstructor
@Log4j2
class PlayerService {
    private final static String PLAYER_NOT_FOUND = "Player not found";
    private final PlayerRepository playerRepository;
    private final ObjectMapper objectMapper;

    private static PlayerResponse toPlayer(PlayerRequest playerRequest, Player playerSaved) {
        return PlayerResponse.builder()
                .id(playerSaved.id())
                .name(playerRequest.name())
                .surname(playerRequest.surname())
                .isCreated(true)
                .result(REGISTER_SUCCESS.name())
                .status(playerSaved.status())
                .build();
    }

    private static Player getPlayer(PlayerRequest playerRequest) {
        return Player.builder()
                .id(UUID.randomUUID())
                .name(playerRequest.name())
                .surname(playerRequest.surname())
                .email(playerRequest.email())
                .createdAt(LocalDateTime.now())
                .status(ACTIVE)
                .build();
    }

    public PlayerResponse registerPlayer(PlayerRequest playerRequest) {
        validatePlayerDoesNotExist(playerRequest);
        Player player = getPlayer(playerRequest);
        Player playerSaved = playerRepository.save(player);
        log.info("Player saved: {}", playerSaved);


        return toPlayer(playerRequest, playerSaved);
    }

    private void validatePlayerDoesNotExist(PlayerRequest playerRequest) {
        String name = playerRequest.name();
        String surname = playerRequest.surname();
        String email = playerRequest.email();
        checkExistingPlayer(playerRequest, name, surname, email);
    }

    private void checkExistingPlayer(PlayerRequest playerRequest, String name, String surname, String email) {
        if (playerRepository.existsByNameAndSurname(name, surname)) {
            throw new PlayerAlreadyExistsException("Player {} {} already exists", name, surname);
        }
        if (playerRepository.existsByEmail(playerRequest.email())) {
            throw new PlayerAlreadyExistsException("Player email {} already exists", email);
        }
    }

    public PlayerResponse findPlayer(UUID playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(PLAYER_NOT_FOUND));

        return objectMapper.convertValue(player, PlayerResponse.class);
    }


    public Set<PlayerResponse> findPlayers() {
        List<Player> players = playerRepository.findAll();
        if (players.isEmpty()) {
            throw new PlayerNotFoundException(PLAYER_NOT_FOUND);
        }
        return players.stream().map(pl -> objectMapper.convertValue(pl, PlayerResponse.class))
                .collect(Collectors.toSet());
    }

    public PlayerResponse updatePlayer(UUID playerId, PlayerRequest playerRequest) {
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

    public void removePlayer(UUID playerId) {
        Player playerToDelete = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(PLAYER_NOT_FOUND + " with id: " + playerId));
        playerRepository.delete(playerToDelete);
        log.info("Player with id {} removed", playerId);
    }
}

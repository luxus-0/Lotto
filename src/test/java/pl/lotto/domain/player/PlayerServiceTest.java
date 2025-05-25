package pl.lotto.domain.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lotto.domain.player.dto.PlayerRequest;
import pl.lotto.domain.player.dto.PlayerResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.lotto.domain.player.PlayerRegisterStatus.REGISTER_SUCCESS;
import static pl.lotto.domain.player.PlayerStatus.ACTIVE;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private ObjectMapper objectMapper; // Mock ObjectMapper as it's an external dependency

    @InjectMocks
    private PlayerService playerService;

    private PlayerRequest playerRequest;
    private Player player;
    private PlayerResponse playerResponse;
    private UUID playerId;

    @BeforeEach
    void setUp() {
        playerId = UUID.randomUUID();
        playerRequest = new PlayerRequest("John", "Doe", "john.doe@example.com");
        player = Player.builder()
                .id(playerId)
                .name("John")
                .surname("Doe")
                .email("john.doe@example.com")
                .createdAt(LocalDateTime.now())
                .status(ACTIVE)
                .build();
        playerResponse = PlayerResponse.builder()
                .id(playerId)
                .name("John")
                .surname("Doe")
                .isCreated(true)
                .result(REGISTER_SUCCESS.name())
                .status(ACTIVE)
                .build();
    }

    @Test
    void should_register_player_successfully() {
        // Given
        when(playerRepository.existsByNameAndSurname(playerRequest.name(), playerRequest.surname())).thenReturn(false);
        when(playerRepository.existsByEmail(playerRequest.email())).thenReturn(false);
        when(playerRepository.save(any(Player.class))).thenReturn(player);

        // When
        PlayerResponse result = playerService.registerPlayer(playerRequest);

        // Then
        assertThat(result.id()).isNotNull();
        assertThat(result.name()).isEqualTo(playerRequest.name());
        assertThat(result.surname()).isEqualTo(playerRequest.surname());
        assertThat(result.isCreated()).isTrue();
        assertThat(result.result()).isEqualTo(REGISTER_SUCCESS.name());
        assertThat(result.status()).isEqualTo(ACTIVE);
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    void should_throw_exception_when_player_with_same_name_and_surname_already_exists() {
        // Given
        when(playerRepository.existsByNameAndSurname(playerRequest.name(), playerRequest.surname())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> playerService.registerPlayer(playerRequest))
                .isInstanceOf(PlayerAlreadyExistsException.class)
                .hasMessageContaining("Player John Doe already exists");
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    void should_throw_exception_when_player_with_same_email_already_exists() {
        // Given
        when(playerRepository.existsByNameAndSurname(playerRequest.name(), playerRequest.surname())).thenReturn(false);
        when(playerRepository.existsByEmail(playerRequest.email())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> playerService.registerPlayer(playerRequest))
                .isInstanceOf(PlayerAlreadyExistsException.class)
                .hasMessageContaining("Player email john.doe@example.com already exists");
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    void should_find_player_by_id_successfully() {
        // Given
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(objectMapper.convertValue(player, PlayerResponse.class)).thenReturn(playerResponse);

        // When
        PlayerResponse result = playerService.findPlayer(playerId);

        // Then
        assertThat(result).isEqualTo(playerResponse);
        verify(playerRepository, times(1)).findById(playerId);
        verify(objectMapper, times(1)).convertValue(player, PlayerResponse.class);
    }

    @Test
    void should_throw_exception_when_player_not_found_by_id() {
        // Given
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> playerService.findPlayer(playerId))
                .isInstanceOf(PlayerNotFoundException.class)
                .hasMessageContaining("Player not found");
        verify(playerRepository, times(1)).findById(playerId);
        verify(objectMapper, never()).convertValue(Player.class, PlayerResponse.class);
    }

    @Test
    void should_find_all_players_successfully() {
        // Given
        List<Player> players = List.of(player,
                Player.builder().id(UUID.randomUUID()).name("Jane").surname("Doe").email("jane.doe@example.com").createdAt(LocalDateTime.now()).status(ACTIVE).build());
        PlayerResponse playerResponse2 = PlayerResponse.builder().id(players.get(1).id()).name("Jane").surname("Doe").isCreated(true).result(REGISTER_SUCCESS.name()).status(ACTIVE).build();

        when(playerRepository.findAll()).thenReturn(players);
        when(objectMapper.convertValue(players.get(0), PlayerResponse.class)).thenReturn(playerResponse);
        when(objectMapper.convertValue(players.get(1), PlayerResponse.class)).thenReturn(playerResponse2);

        // When
        Set<PlayerResponse> results = playerService.findPlayers();

        // Then
        assertThat(results).hasSize(2);
        assertThat(results).containsExactlyInAnyOrder(playerResponse, playerResponse2);
        verify(playerRepository, times(1)).findAll();
    }

    @Test
    void should_throw_exception_when_no_players_found() {
        // Given
        when(playerRepository.findAll()).thenReturn(Collections.emptyList());

        // When & Then
        assertThatThrownBy(() -> playerService.findPlayers())
                .isInstanceOf(PlayerNotFoundException.class)
                .hasMessageContaining("Player not found");
        verify(playerRepository, times(1)).findAll();
        verify(objectMapper, never()).convertValue(Player.class, PlayerResponse.class);
    }

    @Test
    void should_update_player_successfully() {
        // Given
        PlayerRequest updateRequest = new PlayerRequest("Johnny", "English", "johnny.english@example.com");
        Player updatedPlayer = new Player(
                playerId,
                updateRequest.name(),
                updateRequest.surname(),
                updateRequest.email(),
                player.createdAt(),
                player.status()
        );
        PlayerResponse updatedPlayerResponse = PlayerResponse.builder()
                .id(playerId)
                .name(updateRequest.name())
                .surname(updateRequest.surname())
                .isCreated(true) // This might not be true for update, but matching the service's conversion
                .result(REGISTER_SUCCESS.name()) // This might not be true for update, but matching the service's conversion
                .status(ACTIVE)
                .build();

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(playerRepository.save(any(Player.class))).thenReturn(updatedPlayer);
        when(objectMapper.convertValue(updatedPlayer, PlayerResponse.class)).thenReturn(updatedPlayerResponse);

        // When
        PlayerResponse result = playerService.updatePlayer(playerId, updateRequest);

        // Then
        assertThat(result).isEqualTo(updatedPlayerResponse);
        verify(playerRepository, times(1)).findById(playerId);
        verify(playerRepository, times(1)).save(any(Player.class));
        verify(objectMapper, times(1)).convertValue(updatedPlayer, PlayerResponse.class);
    }

    @Test
    void should_throw_exception_when_player_not_found_for_update() {
        // Given
        PlayerRequest updateRequest = new PlayerRequest("Johnny", "English", "johnny.english@example.com");
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> playerService.updatePlayer(playerId, updateRequest))
                .isInstanceOf(PlayerNotFoundException.class)
                .hasMessageContaining("Player not found with id: " + playerId);
        verify(playerRepository, times(1)).findById(playerId);
        verify(playerRepository, never()).save(any(Player.class));
        verify(objectMapper, never()).convertValue(Player.class, PlayerResponse.class);
    }

    @Test
    void should_remove_player_successfully() {
        // Given
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        // When
        playerService.removePlayer(playerId);

        // Then
        verify(playerRepository, times(1)).findById(playerId);
        verify(playerRepository, times(1)).delete(player);
    }

    @Test
    void should_throw_exception_when_player_not_found_for_removal() {
        // Given
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> playerService.removePlayer(playerId))
                .isInstanceOf(PlayerNotFoundException.class)
                .hasMessageContaining("Player not found with id: " + playerId);
        verify(playerRepository, times(1)).findById(playerId);
        verify(playerRepository, never()).delete(any(Player.class));
    }
}

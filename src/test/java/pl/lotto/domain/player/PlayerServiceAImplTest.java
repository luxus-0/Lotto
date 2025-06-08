package pl.lotto.domain.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.lotto.domain.player.dto.PlayerRequest;
import pl.lotto.domain.player.dto.PlayerResponse;
import pl.lotto.domain.player.dto.PlayerStatistics;
import pl.lotto.domain.player.exceptions.PlayerAlreadyExistsException;
import pl.lotto.domain.player.exceptions.PlayerNotFoundException;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static pl.lotto.domain.player.PlayerRegisterStatus.REGISTER_SUCCESS;

class PlayerServiceAImplTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should register player successfully when player not exists")
    void shouldRegisterPlayer() {
        // given
        PlayerRequest request = new PlayerRequest(UUID.randomUUID(), "John", "john@example.com");
        Player player = new Player(UUID.randomUUID(), "John", "Doe", "john@example.com", LocalDateTime.now(), PlayerStatus.ACTIVE);
        when(playerRepository.existsPlayerById(request.id())).thenReturn(false);
        when(playerRepository.save(any())).thenReturn(player);

        // when
        PlayerResponse response = playerService.registerPlayer(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("John");
        assertThat(response.status()).isEqualTo(PlayerStatus.ACTIVE);
        assertThat(response.isCreated()).isTrue();
    }

    @Test
    @DisplayName("Should player status active and is created true when player exists")
    void shouldPlayerStatusActiveAndIsCreatedTrue() {
        PlayerRequest request = new PlayerRequest(UUID.randomUUID(), "John", "john@example.com");
        when(playerRepository.existsPlayerById(request.id())).thenReturn(false);
        Player player = new Player(UUID.randomUUID(), "John", "Doe", "john@example.com", LocalDateTime.now(), PlayerStatus.ACTIVE);
        when(playerRepository.save(any())).thenReturn(player);

        PlayerResponse response = playerService.registerPlayer(request);
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("John");
        assertThat(response.status()).isEqualTo(PlayerStatus.ACTIVE);
        assertThat(response.isCreated()).isTrue();
    }

    @Test
    @DisplayName("Should player status inactive and is created false when player exists")
    void shouldPlayerStatusInactiveAndIsCreatedFalse() {
        PlayerRequest request = new PlayerRequest(UUID.randomUUID(), "John", "john@example.com");
        when(playerRepository.existsPlayerById(request.id())).thenReturn(true);

        PlayerResponse response = playerService.registerPlayer(request);
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("John");
        assertThat(response.status()).isEqualTo(PlayerStatus.INACTIVE);
        assertThat(response.isCreated()).isFalse();
    }

    @Test
    @DisplayName("Should find player by ID")
    void shouldFindPlayerById() {
        UUID playerId = UUID.randomUUID();
        Player player = new Player(playerId, "Jane", "Smith", "jane@example.com", LocalDateTime.now(), PlayerStatus.ACTIVE);
        PlayerResponse expectedResponse = PlayerResponse.builder()
                .name("Jane")
                .status(PlayerStatus.ACTIVE)
                .isCreated(true)
                .build();

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(objectMapper.convertValue(player, PlayerResponse.class)).thenReturn(expectedResponse);

        PlayerResponse response = playerService.findPlayer(playerId);

        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Should throw when player not found by ID")
    void shouldThrowWhenPlayerNotFoundById() {
        UUID playerId = UUID.randomUUID();
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> playerService.findPlayer(playerId))
                .isInstanceOf(PlayerNotFoundException.class);
    }

    @Test
    @DisplayName("Should return all players")
    void shouldFindAllPlayers() {
        Player player = new Player(UUID.randomUUID(), "Anna", "Nowak", "anna@example.com", LocalDateTime.now(), PlayerStatus.ACTIVE);
        PlayerResponse response = PlayerResponse.builder()
                .name("Anna").status(PlayerStatus.ACTIVE)
                .isCreated(true)
                .build();

        when(playerRepository.findAll()).thenReturn(List.of(player));
        when(objectMapper.convertValue(player, PlayerResponse.class)).thenReturn(response);

        Set<PlayerResponse> result = playerService.findPlayers();

        assertThat(result).hasSize(1).contains(response);
    }

    @Test
    @DisplayName("Should throw when no players found")
    void shouldThrowWhenNoPlayersFound() {
        when(playerRepository.findAll()).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> playerService.findPlayers())
                .isInstanceOf(PlayerNotFoundException.class);
    }

    @Test
    @DisplayName("Should update existing player")
    void shouldUpdatePlayer() {
        UUID playerId = UUID.randomUUID();
        Player existing = new Player(playerId, "Tom", "Black", "tom@example.com", LocalDateTime.now(), PlayerStatus.ACTIVE);
        PlayerStatistics statistics = new PlayerStatistics(UUID.randomUUID(), PlayerStatus.ACTIVE, LocalDateTime.now(), LocalDateTime.now().plusDays(2));
        PlayerRequest updatedRequest = new PlayerRequest(UUID.randomUUID(), "Tommy", "tommy@example.com");
        Player updated = new Player(playerId, "Tommy", "Black", "tommy@example.com", existing.createdAt(), PlayerStatus.ACTIVE);
        PlayerResponse expected = PlayerResponse.builder()
                .name("Tommy")
                .isCreated(true)
                .status(PlayerStatus.ACTIVE)
                .build();

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(existing));
        when(playerRepository.save(any())).thenReturn(updated);
        when(objectMapper.convertValue(updated, PlayerResponse.class)).thenReturn(expected);

        PlayerResponse result = playerService.updatePlayer(playerId);

        assertThat(result.name()).isEqualTo("Tommy");
    }

    @Test
    @DisplayName("Should remove player by ID")
    void shouldRemovePlayer() {
        UUID playerId = UUID.randomUUID();
        Player player = new Player(playerId, "Adam", "White", "adam@example.com", LocalDateTime.now(), PlayerStatus.ACTIVE);
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        playerService.removePlayer(playerId);

        verify(playerRepository, times(1)).delete(player);
    }

    @Test
    @DisplayName("Should throw when removing non-existent player")
    void shouldThrowWhenRemovingNonExistentPlayer() {
        UUID playerId = UUID.randomUUID();
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> playerService.removePlayer(playerId))
                .isInstanceOf(PlayerNotFoundException.class);
    }
} 
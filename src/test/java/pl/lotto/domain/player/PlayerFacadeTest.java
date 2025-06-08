package pl.lotto.domain.player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lotto.domain.player.dto.PlayerRequest;
import pl.lotto.domain.player.dto.PlayerResponse;
import pl.lotto.domain.player.dto.PlayerStatistics;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PlayerFacadeTest {

    private PlayerService playerService;
    private PlayerFacade playerFacade;

    @BeforeEach
    void setUp() {
        playerService = mock(PlayerService.class);
        playerFacade = new PlayerFacade(playerService);
    }

    @Test
    void should_delegate_register_request_to_service() {
        // given
        PlayerStatistics playerStatistics = PlayerStatistics.builder()
                .status(PlayerStatus.ACTIVE)
                .lastWinAt(LocalDateTime.now())
                .lastPlayedAt(LocalDateTime.now().minusDays(2))
                .playerId(UUID.randomUUID())
                .build();

        PlayerRequest request = PlayerRequest.builder()
                .id(UUID.randomUUID())
                .name("Player 1")
                .email("lux@o2.pl")
                .build();

        PlayerResponse expectedResponse = PlayerResponse.builder()
                .name("John")
                .isCreated(true)
                .status(PlayerStatus.ACTIVE)
                .build();

        when(playerService.registerPlayer(request)).thenReturn(expectedResponse);

        // when
        PlayerResponse actualResponse = playerFacade.register(request);

        // then
        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(playerService, times(1)).registerPlayer(request);
    }

    @Test
    void should_delegate_find_request_to_service() {
        // given
        UUID playerId = UUID.randomUUID();
        PlayerResponse expected = PlayerResponse.builder()
                .name("Jane")
                .isCreated(true)
                .status(PlayerStatus.ACTIVE)
                .build();

        when(playerService.findPlayer(playerId)).thenReturn(expected);

        // when
        PlayerResponse actual = playerFacade.find(playerId);

        // then
        assertThat(actual).isEqualTo(expected);
        verify(playerService).findPlayer(playerId);
    }

    @Test
    void should_delegate_findAll_to_service() {
        // given
        Set<PlayerResponse> expected = Set.of(
                PlayerResponse.builder()
                        .name("Andrej")
                        .status(PlayerStatus.ACTIVE)
                        .isCreated(true)
                        .build()
        );
        when(playerService.findPlayers()).thenReturn(expected);

        // when
        Set<PlayerResponse> actual = playerFacade.findAll();

        // then
        assertThat(actual).isEqualTo(expected);
        verify(playerService).findPlayers();
    }

    @Test
    void should_delegate_delete_to_service() {
        // given
        UUID playerId = UUID.randomUUID();

        // when
        playerFacade.delete(playerId);

        // then
        verify(playerService).removePlayer(playerId);
    }

    @Test
    void testUpdatePlayer_shouldCallServiceAndReturnUpdatedPlayer() {
        // GIVEN
        UUID playerId = UUID.randomUUID();

        PlayerRequest playerRequest = PlayerRequest.builder()
                .name("UpdatedName")
                .email("updated.email@example.com")
                .build();

        PlayerResponse expectedResponse = PlayerResponse.builder()
                .name("UpdatedName")
                .isCreated(false)
                .status(PlayerStatus.ACTIVE)
                .build();

        when(playerService.updatePlayer(any(UUID.class)))
                .thenReturn(expectedResponse);

        // WHEN
        PlayerResponse actualResponse = playerFacade.updatePlayer(playerId);

        // THEN
        verify(playerService, times(1)).updatePlayer(playerId);

        assertThat(actualResponse).isEqualTo(expectedResponse);


        assertThat(actualResponse.name()).isEqualTo("UpdatedName");
        assertThat(actualResponse.status()).isEqualTo(PlayerStatus.ACTIVE);
    }
}
package pl.lotto.domain.player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lotto.domain.player.dto.PlayerRequest;
import pl.lotto.domain.player.dto.PlayerResponse;

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
        PlayerRequest request = new PlayerRequest("John", "Doe", "john@example.com");
        PlayerResponse expectedResponse = PlayerResponse.builder()
                .id(UUID.randomUUID())
                .name("John")
                .surname("Doe")
                .isCreated(true)
                .result("REGISTER_SUCCESS")
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
                .id(playerId)
                .name("Jane")
                .surname("Doe")
                .isCreated(true)
                .result("REGISTER_SUCCESS")
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
                new PlayerResponse(UUID.randomUUID(), "Adam", "Nowak", true, "REGISTER_SUCCESS", PlayerStatus.ACTIVE)
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
                .surname("UpdatedSurname")
                .email("updated.email@example.com")
                .build();

        PlayerResponse expectedResponse = PlayerResponse.builder()
                .id(playerId)
                .name("UpdatedName")
                .surname("UpdatedSurname")
                .isCreated(false)
                .result("UPDATE_SUCCESS")
                .status(PlayerStatus.ACTIVE)
                .build();

        when(playerService.updatePlayer(any(UUID.class), any(PlayerRequest.class)))
                .thenReturn(expectedResponse);

        // WHEN
        PlayerResponse actualResponse = playerFacade.updatePlayer(playerId, playerRequest);

        // THEN
        verify(playerService, times(1)).updatePlayer(playerId, playerRequest);

        assertThat(actualResponse).isEqualTo(expectedResponse);


        assertThat(actualResponse.id()).isEqualTo(playerId);
        assertThat(actualResponse.name()).isEqualTo("UpdatedName");
        assertThat(actualResponse.surname()).isEqualTo("UpdatedSurname");
        assertThat(actualResponse.status()).isEqualTo(PlayerStatus.ACTIVE);
        assertThat(actualResponse.result()).isEqualTo("UPDATE_SUCCESS");
    }
}
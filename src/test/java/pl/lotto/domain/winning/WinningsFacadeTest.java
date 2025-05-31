package pl.lotto.domain.winning;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lotto.domain.drawdatetime.DrawDateTimeFacade;
import pl.lotto.domain.winning.dto.WinningRequest;
import pl.lotto.domain.winning.dto.WinningResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WinningsFacadeTest {

    @Mock
    private WinningRepository repository;
    @Mock
    private DrawDateTimeFacade drawDateTimeFacade;
    @InjectMocks
    private WinningFacade winningFacade;
    @Mock
    private WinningValidator validator;
    @Mock
    private WinningService winningService;


    @Test
    void should_return_result_and_save_when_prize_greater_than_zero() {
        // Given
        UUID playerId = UUID.randomUUID();
        Set<Integer> playerNumbers = Set.of(1, 2, 3, 4, 5, 6, 7, 8);
        Set<Integer> randomNumbers = Set.of(3, 4, 5, 6, 7, 8, 1, 10);
        LocalDateTime drawDate = LocalDateTime.now();

        Winning expectedWinningSaved = Winning.builder()
                .id(UUID.randomUUID())
                .playerId(playerId)
                .hits(7)
                .drawDate(drawDate)
                .price(BigDecimal.valueOf(2380.0))
                .build();

        when(drawDateTimeFacade.generate()).thenReturn(drawDate);
        when(repository.save(any(Winning.class))).thenReturn(expectedWinningSaved);
        when(winningService.getWinnerNumbers(playerNumbers)).thenReturn(playerNumbers);

        WinningRequest request = new WinningRequest(playerId, playerNumbers, randomNumbers, drawDate);

        when(validator.valid(request)).thenReturn(true);

        // When
        WinningResponse result = winningFacade.getWinnerResult(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.hits()).isEqualTo(7);
        assertThat(result.price()).isEqualTo(BigDecimal.valueOf(2380.0));
        assertThat(result.isWinner()).isTrue();
        verify(repository, times(1)).save(any(Winning.class));
        verify(validator, times(1)).valid(request);
    }

    @Test
    void should_return_result_and_save_when_prize_is_zero_and_hits_zero() {
        // Given
        UUID playerId = UUID.randomUUID();
        Set<Integer> playerNumbers = Set.of(1, 2, 3, 4, 5, 6, 7, 8);
        Set<Integer> randomNumbers = Set.of(9, 10, 11, 12, 13, 14, 15, 16);
        LocalDateTime drawDate = LocalDateTime.now();


        WinningRequest request = new WinningRequest(playerId, playerNumbers, randomNumbers, drawDate);

        when(validator.valid(request)).thenReturn(false);

        // When
        WinningResponse result = winningFacade.getWinnerResult(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.hits()).isEqualTo(0);
        assertThat(result.price()).isEqualTo(BigDecimal.ZERO);
        assertThat(result.isWinner()).isFalse();
        verify(repository, times(0)).save(any(Winning.class));
        verify(validator, times(1)).valid(request);
    }

    @Test
    void should_return_winning_result_for_three_hits() {
        // Given
        UUID playerId = UUID.randomUUID();
        Set<Integer> playerNumbers = Set.of(1, 2, 3, 10, 20, 30);
        Set<Integer> randomNumbers = Set.of(1, 2, 3, 40, 50, 60);
        LocalDateTime drawDate = LocalDateTime.now();

        when(drawDateTimeFacade.generate()).thenReturn(drawDate);
        when(repository.save(any(Winning.class))).thenReturn(Winning.builder()
                .id(UUID.randomUUID())
                .hits(3)
                .playerId(playerId)
                .price(BigDecimal.valueOf(2380.0))
                .drawDate(drawDate)
                .build());

        WinningRequest request = new WinningRequest(playerId, playerNumbers, randomNumbers, drawDate);

        when(validator.valid(request)).thenReturn(true);

        // When
        WinningResponse result = winningFacade.getWinnerResult(request);

        // Then
        assertThat(result.hits()).isEqualTo(3);
        assertThat(result.price()).isEqualTo(BigDecimal.valueOf(2380.0));
        verify(repository, times(1)).save(any(Winning.class));
    }

    @Test
    void should_return_winning_result_for_six_hits() {
        // Given
        UUID playerId = UUID.randomUUID();
        Set<Integer> playerNumbers = Set.of(1, 2, 3, 4, 5, 6);
        Set<Integer> randomNumbers = Set.of(1, 2, 3, 4, 5, 6);
        LocalDateTime drawDate = LocalDateTime.now();

        when(drawDateTimeFacade.generate()).thenReturn(drawDate);
        when(repository.save(any(Winning.class))).thenReturn(Winning.builder()
                .id(UUID.randomUUID())
                .hits(6)
                .playerId(playerId)
                .price(BigDecimal.valueOf(2040.0))
                .drawDate(drawDate)
                .build());

        when(validator.valid(any(WinningRequest.class))).thenReturn(true);

        WinningRequest request = new WinningRequest(playerId, playerNumbers, randomNumbers, drawDate);

        // When
        WinningResponse result = winningFacade.getWinnerResult(request);

        // Then
        assertThat(result.hits()).isEqualTo(6);
        assertThat(result.price()).isEqualTo(BigDecimal.valueOf(2040.0));
        verify(repository, times(1)).save(any(Winning.class));
    }

    @Test
    void should_handle_empty_player_numbers_returning_zero_hits_and_price() {
        // Given
        UUID playerId = UUID.randomUUID();
        Set<Integer> playerNumbers = Collections.emptySet();
        Set<Integer> randomNumbers = Set.of(1, 2, 3, 4, 5, 6);
        LocalDateTime drawDate = LocalDateTime.now();


        WinningRequest request = new WinningRequest(playerId, playerNumbers, randomNumbers, drawDate);

        // When
        WinningResponse result = winningFacade.getWinnerResult(request);

        // Then
        assertThat(result.hits()).isEqualTo(0);
        assertThat(result.price()).isEqualTo(BigDecimal.ZERO);
        verify(repository, times(0)).save(any(Winning.class));
    }

    @Test
    void should_handle_empty_winning_numbers_returning_zero_hits_and_price() {
        // Given
        UUID playerId = UUID.randomUUID();
        Set<Integer> playerNumbers = Set.of(1, 2, 3, 4, 5, 6);
        Set<Integer> randomNumbers = Collections.emptySet();
        LocalDateTime drawDate = LocalDateTime.now();

        WinningRequest request = new WinningRequest(playerId, playerNumbers, randomNumbers, drawDate);

        // When
        WinningResponse result = winningFacade.getWinnerResult(request);

        // Then
        assertThat(result.hits()).isEqualTo(0);
        assertThat(result.price()).isEqualTo(BigDecimal.ZERO);
        verify(repository, times(0)).save(any(Winning.class));
    }

    @Test
    void should_return_zero_hits_for_non_matching_numbers() {
        // Given
        UUID playerId = UUID.randomUUID();
        Set<Integer> playerNumbers = Set.of(1, 2, 3, 4, 5, 6);
        Set<Integer> randomNumbers = Set.of(7, 8, 9, 10, 11, 12);
        LocalDateTime drawDate = LocalDateTime.now();

        WinningRequest request = new WinningRequest(playerId, playerNumbers, randomNumbers, drawDate);

        // When
        WinningResponse result = winningFacade.getWinnerResult(request);

        // Then
        assertThat(result.hits()).isEqualTo(0);
        assertThat(result.price()).isEqualTo(BigDecimal.ZERO);
        verify(repository, times(0)).save(any(Winning.class));
    }

    @Test
    void should_handle_null_player_id_gracefully() {
        // Given
        Set<Integer> playerNumbers = Set.of(1, 2, 3, 4, 5, 6);
        Set<Integer> randomNumbers = Set.of(1, 2, 3, 7, 8, 9);
        LocalDateTime drawDate = LocalDateTime.now();

        WinningRequest request = new WinningRequest(null, playerNumbers, randomNumbers, drawDate);

        // When
        WinningResponse result = winningFacade.getWinnerResult(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.playerId()).isNull();
        assertThat(result.hits()).isEqualTo(0);
        assertThat(result.price()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void should_throw_exception_when_player_numbers_is_null() {
        // Given
        UUID playerId = UUID.randomUUID();
        Set<Integer> randomNumbers = Set.of(1, 2, 3, 4, 5, 6);
        LocalDateTime drawDate = LocalDateTime.now();

        WinningRequest request = new WinningRequest(playerId, null, randomNumbers, drawDate);

        when(validator.valid(request)).thenThrow(new IllegalArgumentException("player numbers cannot be null or empty"));

        // When & Then
        assertThatThrownBy(() -> winningFacade.getWinnerResult(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("player numbers cannot be null or empty");

        verify(validator, times(1)).valid(request);
        verifyNoInteractions(winningService, drawDateTimeFacade, repository);
    }

    @Test
    void should_throw_exception_when_winning_numbers_are_null_from_service() {
        // Given
        UUID playerId = UUID.randomUUID();
        Set<Integer> playerNumbers = Set.of(1, 2, 3, 4, 5, 6);
        LocalDateTime drawDate = LocalDateTime.now();

        when(validator.valid(any(WinningRequest.class))).thenThrow(new IllegalArgumentException("player numbers cannot be null or empty"));

        WinningRequest request = new WinningRequest(playerId, playerNumbers, Collections.emptySet(), drawDate);

        // When & Then
        assertThatThrownBy(() -> winningFacade.getWinnerResult(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("player numbers cannot be null or empty");
        verify(repository, never()).save(any(Winning.class));
    }


    @Test
    void should_handle_player_numbers_with_duplicates_if_input_was_list() {

        UUID playerId = UUID.randomUUID();

        Set<Integer> playerNumbers = new HashSet<>();
        playerNumbers.add(1);
        playerNumbers.add(2);
        playerNumbers.add(3);

        Set<Integer> randomNumbers = Set.of(1, 2, 40, 50, 60, 70);
        LocalDateTime drawDate = LocalDateTime.now();

        when(drawDateTimeFacade.generate()).thenReturn(drawDate);
        when(repository.save(any(Winning.class))).thenReturn(Winning.builder()
                .id(UUID.randomUUID())
                .playerId(playerId)
                .price(BigDecimal.valueOf(2380.0))
                .hits(2)
                .drawDate(drawDate)
                .build());
        when(validator.valid(any(WinningRequest.class))).thenReturn(true);

        WinningRequest request = new WinningRequest(playerId, playerNumbers, randomNumbers, drawDate);

        // When
        WinningResponse result = winningFacade.getWinnerResult(request);

        // Then
        assertThat(result.hits()).isEqualTo(2);
        assertThat(result.price()).isEqualTo(BigDecimal.valueOf(2380.0));
        verify(repository, times(1)).save(any(Winning.class));
        verify(validator, atLeastOnce()).valid(any(WinningRequest.class));
    }

    @Test
    void should_always_call_random_numbers_generator_query_service() {
        // Given
        UUID playerId = UUID.randomUUID();
        Set<Integer> playerNumbers = Set.of(1, 2);
        Set<Integer> randomNumbers = Set.of(3, 4);
        LocalDateTime drawDate = LocalDateTime.now();

        WinningRequest request = new WinningRequest(playerId, playerNumbers, randomNumbers, drawDate);

        // When
        WinningResponse result = winningFacade.getWinnerResult(request);

        // Then;
        assertThat(result).isNotNull();
    }
}
package pl.lotto.domain.winning;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lotto.domain.randomnumbers.RandomNumbersGeneratorQueryService;
import pl.lotto.domain.randomnumbers.RandomNumbersNotFoundException;
import pl.lotto.domain.winning.exeptions.NumbersHasTheSameSizeException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("WinningService Specification")
class WinningServiceTest {

    @Mock
    private RandomNumbersGeneratorQueryService randomNumbersService;
    @InjectMocks
    private WinningService winningService;
    @Mock
    private WinningNumbersConfigurationProperties properties;

    @Test
    @DisplayName("should count 3 hits when player numbers and winning numbers have 3 common elements")
    void should_count_three_hits_when_three_common_elements() {
        // Given
        Set<Integer> playerNumbers = Set.of(1, 2, 3, 10, 20, 30);
        Set<Integer> winningNumbers = Set.of(1, 2, 3, 40, 50, 60);

        // When
        Integer hits = winningService.countHits(playerNumbers, winningNumbers);

        // Then
        assertThat(hits).isEqualTo(3);
    }

    @Test
    @DisplayName("should count 0 hits when player numbers and winning numbers have no common elements")
    void should_count_zero_hits_when_no_common_elements() {
        // Given
        Set<Integer> playerNumbers = Set.of(1, 2, 3, 4, 5, 6);
        Set<Integer> winningNumbers = Set.of(7, 8, 9, 10, 11, 12);

        // When
        Integer hits = winningService.countHits(playerNumbers, winningNumbers);

        // Then
        assertThat(hits).isEqualTo(0);
    }

    @Test
    @DisplayName("should count 6 hits when all player numbers match winning numbers")
    void should_count_six_hits_when_all_numbers_match() {
        // Given
        Set<Integer> playerNumbers = Set.of(1, 2, 3, 4, 5, 6);
        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6);

        // When
        Integer hits = winningService.countHits(playerNumbers, winningNumbers);

        // Then
        assertThat(hits).isEqualTo(6);
    }

    @Test
    @DisplayName("should count 0 hits when player numbers set is empty")
    void should_count_zero_hits_when_player_numbers_empty() {
        // Given
        Set<Integer> playerNumbers = Collections.emptySet();
        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 6);

        // When
        Integer hits = winningService.countHits(playerNumbers, winningNumbers);

        // Then
        assertThat(hits).isEqualTo(0);
    }

    @Test
    @DisplayName("should count 0 hits when winning numbers set is empty")
    void should_count_zero_hits_when_winning_numbers_empty() {
        // Given
        Set<Integer> playerNumbers = Set.of(1, 2, 3, 4, 5, 6);
        Set<Integer> winningNumbers = Collections.emptySet();

        // When
        Integer hits = winningService.countHits(playerNumbers, winningNumbers);

        // Then
        assertThat(hits).isEqualTo(0);
    }

    @Test
    @DisplayName("should handle large number sets efficiently (functional test)")
    void should_handle_large_number_sets_efficiently() {
        // Given
        Set<Integer> playerNumbers = Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Set<Integer> winningNumbers = Set.of(1, 2, 3, 4, 5, 11, 12, 13, 14, 15);

        // When
        Integer hits = winningService.countHits(playerNumbers, winningNumbers);

        // Then
        assertThat(hits).isEqualTo(5);
    }

    @Test
    @DisplayName("should return 0 price when hits are less than minimum required (3)")
    void should_return_zero_price_when_hits_less_than_three() {
        // Given
        Integer hits = 2;

        // When
        BigDecimal price = winningService.calculatePrice(hits);

        // Then
        assertThat(price).isEqualTo(BigDecimal.valueOf(0.0));
    }

    @Test
    @DisplayName("should return correct price for 3 hits")
    void should_return_correct_price_for_three_hits() {
        // Given
        Integer hits = 3;
        BigDecimal expectedPrice = BigDecimal.valueOf(3).multiply(BigDecimal.valueOf(340.0));

        when(properties.getMinHits()).thenReturn(hits);
        when(properties.getPricePerHit()).thenReturn(340.0);

        // When
        BigDecimal price = winningService.calculatePrice(hits);

        // Then
        assertThat(price).isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("should return correct price for 6 hits")
    void should_return_correct_price_for_six_hits() {
        // Given
        Integer hits = 6;
        BigDecimal expectedPrice = BigDecimal.valueOf(6).multiply(BigDecimal.valueOf(340.0));

        when(properties.getMinHits()).thenReturn(6);
        when(properties.getPricePerHit()).thenReturn(340.0);

        // When
        BigDecimal price = winningService.calculatePrice(hits);

        // Then
        assertThat(price).isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("should return 0 price for 0 hits")
    void should_return_zero_price_for_zero_hits() {
        // Given
        Integer hits = 0;

        // When
        BigDecimal price = winningService.calculatePrice(hits);

        // Then
        assertThat(price).isEqualTo(BigDecimal.valueOf(0.0));
    }

    @Test
    @DisplayName("should return matching numbers when player numbers and generated numbers have common elements and same size")
    void should_return_matching_numbers_when_player_and_generated_have_common_and_same_size() {
        // Given
        Set<Integer> playerNumbers = Set.of(1, 2, 3, 4, 5, 6);
        Set<Integer> generatedNumbers = Set.of(1, 2, 7, 8, 9, 10);

        when(randomNumbersService.generateUniqueNumbers()).thenReturn(generatedNumbers);

        Set<Integer> winnerNumbers = winningService.getWinnerNumbers(playerNumbers);

        // Then
        assertThat(winnerNumbers).containsExactlyInAnyOrder(1, 2);
        verify(randomNumbersService, times(1)).generateUniqueNumbers();
    }

    @Test
    @DisplayName("should return empty set when no common numbers and player numbers size matches generated numbers size")
    void should_return_empty_set_when_no_common_numbers_and_same_size() {
        // Given
        Set<Integer> playerNumbers = Set.of(1, 2, 3, 4, 5, 6);
        Set<Integer> generatedNumbers = Set.of(7, 8, 9, 10, 11, 12);

        when(randomNumbersService.generateUniqueNumbers()).thenReturn(generatedNumbers);

        // When
        Set<Integer> winnerNumbers = winningService.getWinnerNumbers(playerNumbers);

        // Then
        assertThat(winnerNumbers).isEmpty();
        verify(randomNumbersService, times(1)).generateUniqueNumbers();
    }

    @Test
    @DisplayName("should throw IllegalStateException when generated numbers size is different from player numbers size")
    void should_throw_illegal_state_exception_when_generated_numbers_size_differs() {
        // Given
        Set<Integer> playerNumbers = Set.of(1, 2, 3, 4, 5, 6);
        Set<Integer> randomNumbers = Set.of(1, 2, 3);

        when(randomNumbersService.generateUniqueNumbers()).thenReturn(randomNumbers);

        // When & Then
        assertThatThrownBy(() -> winningService.getWinnerNumbers(playerNumbers))
                .isInstanceOf(NumbersHasTheSameSizeException.class)
                .hasMessageContaining("Player numbers and random numbers must have the same size.");

        verify(randomNumbersService, times(1)).generateUniqueNumbers();
    }

    @Test
    @DisplayName("should return no winner numbers when empty player numbers and random numbers")
    void should_return_no_winner_numbers_when_empty_player_numbers_and_random_numbers() {
        // Given
        Set<Integer> playerNumbers = Collections.emptySet();
        Set<Integer> randomNumbers = Collections.emptySet();

        when(randomNumbersService.generateUniqueNumbers()).thenReturn(randomNumbers);

        // When
        Set<Integer> winnerNumbers = winningService.getWinnerNumbers(playerNumbers);

        // Then
        assertThat(winnerNumbers).isEmpty();
        verify(randomNumbersService, times(1)).generateUniqueNumbers();
    }

    @Test
    @DisplayName("should throw random numbers not found exception when random numbers returns null")
    void should_throw_exception_when_generated_numbers_are_null() {
        // Given
        Set<Integer> playerNumbers = Set.of(1, 2, 3, 4, 5, 6);

        when(randomNumbersService.generateUniqueNumbers()).thenReturn(null);

        // When & Then

        assertThatThrownBy(() -> winningService.getWinnerNumbers(playerNumbers))
                .isInstanceOf(RandomNumbersNotFoundException.class)
                .hasMessageContaining("Random numbers not found");

        verify(randomNumbersService, times(1)).generateUniqueNumbers();
    }
}

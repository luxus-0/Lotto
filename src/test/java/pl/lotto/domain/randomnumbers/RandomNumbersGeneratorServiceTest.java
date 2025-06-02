package pl.lotto.domain.randomnumbers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lotto.domain.randomnumbers.exceptions.RandomNumbersNotFoundException;
import pl.lotto.domain.randomnumbers.exceptions.RandomNumbersOutOfBoundsException;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RandomNumbersGeneratorServiceTest {

    @Mock
    private RandomNumbersValidatorConfigurationProperties properties;

    @Mock
    private RandomNumbersRepository randomNumbersRepository;

    @Mock
    private RandomNumbersValidator validator;

    @InjectMocks
    private RandomNumbersGeneratorService randomNumbersService;

    @Test
    void shouldGenerateCorrectAmountOfUniqueNumbersInRange() {
        // given
        when(properties.getMin()).thenReturn(1);
        when(properties.getMax()).thenReturn(49);
        when(properties.getCount()).thenReturn(6);

        // when
        Set<Integer> result = randomNumbersService.generateUniqueNumbers();

        // then
        assertEquals(6, result.size());
        assertTrue(result.stream().allMatch(n -> n >= 1 && n <= 49));
    }

    @Test
    void shouldThrowExceptionWhenCountExceedsRange() {
        // given
        when(properties.getMin()).thenReturn(1);
        when(properties.getMax()).thenReturn(5);
        when(properties.getCount()).thenReturn(10);

        // expect
        assertThrows(RandomNumbersOutOfBoundsException.class, () -> randomNumbersService.generateUniqueNumbers());
    }

    @Test
    void shouldSaveNumbersWhenValidationPasses() {
        // given
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);
        when(properties.getMin()).thenReturn(1);
        when(properties.getMax()).thenReturn(49);
        when(properties.getCount()).thenReturn(6);
        when(validator.validate(Mockito.anySet())).thenReturn(true);
        when(randomNumbersRepository.save(Mockito.anySet())).thenReturn(numbers);

        // when
        randomNumbersService.generate();
        // then
        verify(randomNumbersRepository).save(Mockito.anySet());
    }

    @Test
    void shouldThrowExceptionWhenValidationFails() {
        // given
        when(properties.getMin()).thenReturn(1);
        when(properties.getMax()).thenReturn(49);
        when(properties.getCount()).thenReturn(6);
        when(validator.validate(Mockito.anySet())).thenReturn(false);

        // when && then
        assertThrows(RandomNumbersNotFoundException.class, () -> randomNumbersService.generate());
    }

    @Test
    @DisplayName("should generate a set of unique numbers within the specified range")
    void shouldGenerateUniqueNumbersWithinRange() {
        // Given
        when(properties.getMin()).thenReturn(1);
        when(properties.getMax()).thenReturn(99);
        when(properties.getCount()).thenReturn(7);

        // When
        Set<Integer> uniqueNumbers = randomNumbersService.generateUniqueNumbers();

        // Then
        assertThat(uniqueNumbers).isNotNull();
        assertThat(uniqueNumbers.size()).isEqualTo(7);
        assertThat(uniqueNumbers).allMatch(number -> number >= 1 && number <= 99);
        assertThat(uniqueNumbers).doesNotHaveDuplicates();
    }

    @Test
    @DisplayName("should throw RandomNumbersOutOfBoundsException when count is greater than range")
    void shouldThrowRandomNumbersOutOfBoundsExceptionWhenCountIsGreaterThanRange() {
        // Given
        when(properties.getMin()).thenReturn(1);
        when(properties.getMax()).thenReturn(5);
        when(properties.getCount()).thenReturn(6);

        // When & Then
        assertThatThrownBy(() -> randomNumbersService.generateUniqueNumbers())
                .isInstanceOf(RandomNumbersOutOfBoundsException.class)
                .hasMessage("Random numbers out of bounds");
    }

    @Test
    @DisplayName("should successfully generate and save random numbers when valid")
    void shouldGenerateAndSaveRandomNumbersWhenValid() {
        // Given
        when(properties.getMin()).thenReturn(1);
        when(properties.getMax()).thenReturn(6);
        when(properties.getCount()).thenReturn(6);
        when(validator.validate(anySet())).thenReturn(true);
        when(randomNumbersRepository.save(anySet())).thenReturn(new LinkedHashSet<>(Set.of(1, 2, 3, 4, 5, 6)));

        // When
        randomNumbersService.generate();

        // Then
        verify(randomNumbersRepository, times(1)).save(anySet());
        verify(validator, times(1)).validate(anySet());
    }

    @Test
    @DisplayName("should throw RandomNumbersNotFoundException when generated numbers are invalid")
    void shouldThrowRandomNumbersNotFoundExceptionWhenInvalid() {
        // Given
        when(properties.getMin()).thenReturn(1);
        when(properties.getMax()).thenReturn(6);
        when(properties.getCount()).thenReturn(6);

        when(validator.validate(anySet())).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> randomNumbersService.generate())
                .isInstanceOf(RandomNumbersNotFoundException.class)
                .hasMessage("Random numbers not found");

        verify(randomNumbersRepository, never()).save(any());
        verify(validator, times(1)).validate(anySet());
    }

    @Test
    @DisplayName("should ensure uniqueness of generated numbers for a small range")
    void shouldEnsureUniquenessForSmallRange() {
        // Given
        when(properties.getMin()).thenReturn(1);
        when(properties.getMax()).thenReturn(6);
        when(properties.getCount()).thenReturn(6);

        // When
        Set<Integer> uniqueNumbers = randomNumbersService.generateUniqueNumbers();

        // Then
        assertThat(uniqueNumbers).hasSize(6);
        assertThat(uniqueNumbers).containsExactlyInAnyOrderElementsOf(
                IntStream.rangeClosed(1, 6).boxed().collect(Collectors.toSet())
        );
    }

    @Test
    @DisplayName("should ensure different numbers are generated on subsequent calls (due to randomness)")
    void shouldEnsureDifferentNumbersOnSubsequentCalls() {
        // Given
        when(properties.getMin()).thenReturn(1);
        when(properties.getMax()).thenReturn(99);
        when(properties.getCount()).thenReturn(6);

        // When
        Set<Integer> numbers1 = randomNumbersService.generateUniqueNumbers();
        Set<Integer> numbers2 = randomNumbersService.generateUniqueNumbers();

        assertThat(numbers1).isNotEqualTo(numbers2);
    }
}
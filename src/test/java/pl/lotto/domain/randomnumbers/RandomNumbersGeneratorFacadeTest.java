package pl.lotto.domain.randomnumbers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Set;
import java.util.LinkedHashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RandomNumbersGeneratorFacadeTest {

    @Mock
    private RandomNumbersGeneratorService randomNumbersGeneratorService;

    @InjectMocks
    private RandomNumbersGeneratorFacade randomNumbersGeneratorFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGenerateAndReturnRandomNumbersSuccessfully() {
        // given
        Set<Integer> expectedRandomNumbers = new LinkedHashSet<>(Set.of(1, 2, 3, 4, 5, 6));
        when(randomNumbersGeneratorService.generate()).thenReturn(expectedRandomNumbers);

        // when
        Set<Integer> actualRandomNumbers = randomNumbersGeneratorFacade.generate();

        // then
        verify(randomNumbersGeneratorService, times(1)).generate();
        assertThat(actualRandomNumbers).isEqualTo(expectedRandomNumbers);
        assertThat(actualRandomNumbers).hasSize(6);
    }

    @Test
    void shouldPropagateExceptionWhenServiceThrowsException() {
        // given
        RuntimeException expectedException = new RuntimeException("Service error during generation!");
        when(randomNumbersGeneratorService.generate()).thenThrow(expectedException);

        // when + then
        assertThatThrownBy(() -> randomNumbersGeneratorFacade.generate())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Service error during generation!");
        verify(randomNumbersGeneratorService, times(1)).generate();
    }

    @Test
    void shouldReturnEmptySetWhenServiceReturnsEmptySet() {
        // given
        Set<Integer> expectedEmptySet = new LinkedHashSet<>();
        when(randomNumbersGeneratorService.generate()).thenReturn(expectedEmptySet);

        // when
        Set<Integer> actualRandomNumbers = randomNumbersGeneratorFacade.generate();

        // then
        verify(randomNumbersGeneratorService, times(1)).generate();
        assertThat(actualRandomNumbers).isEmpty();
    }

    @Test
    void shouldReturnNullWhenServiceReturnsNull() {
        // given
        when(randomNumbersGeneratorService.generate()).thenReturn(null);

        // when
        Set<Integer> actualRandomNumbers = randomNumbersGeneratorFacade.generate();

        // then
        verify(randomNumbersGeneratorService, times(1)).generate();
        assertThat(actualRandomNumbers).isNull();
    }

    @Test
    void shouldReturnNumbersWithUnexpectedSizeWhenServiceReturnsThem() {
        // given
        Set<Integer> unexpectedSizeNumbers = new LinkedHashSet<>(Set.of(1, 2, 3));
        when(randomNumbersGeneratorService.generate()).thenReturn(unexpectedSizeNumbers);

        // when
        Set<Integer> actualRandomNumbers = randomNumbersGeneratorFacade.generate();

        // then
        verify(randomNumbersGeneratorService, times(1)).generate();
        assertThat(actualRandomNumbers).isEqualTo(unexpectedSizeNumbers);
        assertThat(actualRandomNumbers).hasSize(3);
    }
}
package pl.lotto.domain.randomnumbers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RandomNumbersGeneratorQueryServiceAImplTest {

    @Mock
    private RandomNumbersValidatorConfigurationProperties properties;

    @Mock
    private RandomNumbersRepository randomNumbersRepository;

    @Mock
    private RandomNumbersValidator validator;

    @InjectMocks
    private RandomNumbersGeneratorQueryServiceImpl service;

    @Test
    void shouldGenerateCorrectAmountOfUniqueNumbersInRange() {
        // given
        Mockito.when(properties.min()).thenReturn(1);
        Mockito.when(properties.max()).thenReturn(49);
        Mockito.when(properties.count()).thenReturn(6);

        // when
        Set<Integer> result = service.generateUniqueNumbers();

        // then
        assertEquals(6, result.size());
        assertTrue(result.stream().allMatch(n -> n >= 1 && n <= 49));
    }

    @Test
    void shouldThrowExceptionWhenCountExceedsRange() {
        // given
        Mockito.when(properties.min()).thenReturn(1);
        Mockito.when(properties.max()).thenReturn(5);
        Mockito.when(properties.count()).thenReturn(10);

        // expect
        assertThrows(RandomNumbersOutOfBoundsException.class, () -> service.generateUniqueNumbers());
    }

    @Test
    void shouldSaveNumbersWhenValidationPasses() {
        // given
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);
        Mockito.when(properties.min()).thenReturn(1);
        Mockito.when(properties.max()).thenReturn(49);
        Mockito.when(properties.count()).thenReturn(6);
        Mockito.when(validator.validate(Mockito.anySet())).thenReturn(true);
        Mockito.when(randomNumbersRepository.save(Mockito.anySet())).thenReturn(numbers);

        // when
        service.generate();
        // then
        Mockito.verify(randomNumbersRepository).save(Mockito.anySet());
    }

    @Test
    void shouldThrowExceptionWhenValidationFails() {
        // given
        Mockito.when(properties.min()).thenReturn(1);
        Mockito.when(properties.max()).thenReturn(49);
        Mockito.when(properties.count()).thenReturn(6);
        Mockito.when(validator.validate(Mockito.anySet())).thenReturn(false);

        // when && then
        assertThrows(RandomNumbersNotFoundException.class, () -> service.generate());
    }
}
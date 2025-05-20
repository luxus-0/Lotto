package pl.lotto.domain.randomnumbers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import pl.lotto.domain.drawdatetime.DrawDateTimeService;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RandomNumbersSchedulerServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private RandomNumbersConfigurationProperties properties;

    @Mock
    private RandomNumbersRepository repository;

    @Mock
    private DrawDateTimeService drawDateTimeService;

    @InjectMocks
    private RandomNumbersSchedulerService schedulerService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateLottery_shouldSendMessagesAndSaveNumbers() throws InterruptedException {
        // given
        when(properties.minNumber()).thenReturn(1);
        when(properties.maxNumber()).thenReturn(10);
        when(properties.countNumbers()).thenReturn(5);
        when(properties.countDownSeconds()).thenReturn(1);
        when(properties.delayBetweenDrawNumbersMillis()).thenReturn(1);

        LocalDateTime drawDateTime = LocalDateTime.now().plusDays(1);
        when(drawDateTimeService.generateDrawDateTime()).thenReturn(drawDateTime);

        when(repository.save(any(RandomNumbers.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        schedulerService.generateLottery();

        // then
        verify(messagingTemplate).convertAndSend("/random_numbers/start", "start");
        verify(messagingTemplate, atLeastOnce()).convertAndSend(eq("/random_numbers/draw"), anyInt());
        verify(messagingTemplate).convertAndSend(eq("/random_numbers/result"), anySet());
        verify(messagingTemplate).convertAndSend("/random_numbers/end", "end");

        verify(repository).save(any(RandomNumbers.class));
        verify(drawDateTimeService).generateDrawDateTime();
    }

    @Test
    void validateNumberRange_shouldThrowException_whenMinIsGreaterThanMax() {
        when(properties.minNumber()).thenReturn(10);
        when(properties.maxNumber()).thenReturn(5);

        RandomNumbersOutOfBoundsException exception = assertThrows(RandomNumbersOutOfBoundsException.class,
                () -> schedulerService.generateLottery());

        assertEquals("Min number cannot be greater than max number", exception.getMessage());
    }

    @Test
    void drawNumbers_shouldGenerateCorrectCountOfUniqueNumbers() {
        when(properties.minNumber()).thenReturn(1);
        when(properties.maxNumber()).thenReturn(20);
        when(properties.countNumbers()).thenReturn(6);

        Set<Integer> numbers = schedulerService.drawNumbers();

        assertEquals(6, numbers.size());
        numbers.forEach(n -> assertTrue(n >= 1 && n <= 20));
    }

    @Test
    void countDown_shouldSendCorrectCountdownMessages() throws InterruptedException {
        // given
        when(properties.countDownSeconds()).thenReturn(5);
        when(properties.delayBetweenDrawNumbersMillis()).thenReturn(5);

        // when
        schedulerService.countDown();

        // then
        verify(messagingTemplate).convertAndSend("/random_numbers/countdown", 3);
        verify(messagingTemplate).convertAndSend("/random_numbers/countdown", 2);
        verify(messagingTemplate).convertAndSend("/random_numbers/countdown", 1);
        verify(messagingTemplate).convertAndSend("/random_numbers/countdown", 0);
    }

    @Test
    void sendDrawNumbers_shouldSendEachNumberWithDelay() throws InterruptedException {
        // Given
        when(properties.delayBetweenDrawNumbersMillis()).thenReturn(1);

        Set<Integer> numbers = Set.of(4, 7, 9);

        // When
        schedulerService.sendDrawNumbers(numbers);

        // Then
        for (Integer number : numbers) {
            verify(messagingTemplate).convertAndSend("/random_numbers/draw", number);
        }
    }

    @Test
    void drawNumbers_shouldGenerateUniqueNumbers() {
        when(properties.minNumber()).thenReturn(1);
        when(properties.maxNumber()).thenReturn(5);
        when(properties.countNumbers()).thenReturn(5);

        Set<Integer> numbers = schedulerService.drawNumbers();

        assertEquals(5, numbers.size());
        assertTrue(numbers.stream().allMatch(n -> n >= 1 && n <= 5));
    }

    @Test
    void drawNumbers_shouldHandleWhenCountIsMoreThanRange() {
        when(properties.minNumber()).thenReturn(1);
        when(properties.maxNumber()).thenReturn(3);
        when(properties.countNumbers()).thenReturn(5);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> schedulerService.drawNumbers());

        assertEquals("Requested countNumbers is greater than number range", exception.getMessage());
    }

    @Test
    void generateLottery_shouldLogCorrectMessages() throws InterruptedException {
        when(properties.minNumber()).thenReturn(1);
        when(properties.maxNumber()).thenReturn(10);
        when(properties.countNumbers()).thenReturn(3);
        when(properties.countDownSeconds()).thenReturn(0);
        when(properties.delayBetweenDrawNumbersMillis()).thenReturn(1);

        LocalDateTime date = LocalDateTime.now().plusDays(1);
        when(drawDateTimeService.generateDrawDateTime()).thenReturn(date);
        when(repository.save(any(RandomNumbers.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RandomNumbersSchedulerService spyService = spy(schedulerService);
        doNothing().when(spyService).countDown();
        doNothing().when(spyService).sendDrawNumbers(anySet());

        spyService.generateLottery();
    }
}

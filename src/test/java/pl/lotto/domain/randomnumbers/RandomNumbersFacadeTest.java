package pl.lotto.domain.randomnumbers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RandomNumbersFacadeTest {

    @Mock
    private RandomNumbersSchedulerService randomNumbersSchedulerService;

    @InjectMocks
    private RandomNumbersFacade drawFacade;

    @Test
    void shouldGenerateRandomNumbers() throws InterruptedException {
    }

}
package pl.lotto.domain.randomnumbers;

import org.springframework.scheduling.annotation.Scheduled;

public interface RandomNumberGeneratorQueryService {
    @Scheduled(cron = "${random.numbers.cron}")
    void generate();
}

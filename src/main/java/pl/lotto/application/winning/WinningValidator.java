package pl.lotto.application.winning;

import org.springframework.stereotype.Service;
import pl.lotto.application.randomnumbers.exceptions.RandomNumbersNotFoundException;
import pl.lotto.application.winning.dto.WinningRequest;
import pl.lotto.application.winning.exeptions.PlayerIdNotFoundException;
import pl.lotto.application.winning.exeptions.PlayerNumbersNotFoundExceptions;
import pl.lotto.application.winning.exeptions.WinningDateNotFoundException;

@Service
class WinningValidator {
    boolean valid(WinningRequest winningRequest) {
        if (winningRequest.playerNumbers() == null || winningRequest.playerNumbers().isEmpty()) {
            throw new PlayerNumbersNotFoundExceptions("player numbers cannot be null or empty");
        }
        if (winningRequest.randomNumbers() == null || winningRequest.randomNumbers().isEmpty()) {
            throw new RandomNumbersNotFoundException("Random numbers cannot be null or empty");
        }
        if (winningRequest.dateTime() == null) {
            throw new WinningDateNotFoundException("Winning date not found");
        }
        if (winningRequest.playerId() == null) {
            throw new PlayerIdNotFoundException("Player id cannot be null");
        }
        return true;
    }
}

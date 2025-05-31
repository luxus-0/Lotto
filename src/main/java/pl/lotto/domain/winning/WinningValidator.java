package pl.lotto.domain.winning;

import org.springframework.stereotype.Service;

@Service
class WinningValidator {
    boolean valid(WinningRequest winningRequest) {
        if (winningRequest.playerNumbers() == null || winningRequest.playerNumbers().isEmpty()) {
            throw new IllegalArgumentException("player numbers cannot be null or empty");
        }
        if (winningRequest.drawNumbers() == null || winningRequest.drawNumbers().isEmpty()) {
            throw new IllegalArgumentException("Draw numbers cannot be null or empty");
        }
        if (winningRequest.drawDate() == null) {
            throw new IllegalArgumentException("Draw date cannot be null");
        }
        if (winningRequest.playerId() == null) {
            throw new IllegalArgumentException("Player id cannot be null");
        }
        return true;
    }
}

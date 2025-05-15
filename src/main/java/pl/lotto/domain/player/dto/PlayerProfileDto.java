package pl.lotto.domain.player.dto;

public record PlayerProfileDto(
        Integer totalGamesPlayed,
        Integer totalWins,
        String description,
        String imageUrl) {
}

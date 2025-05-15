package pl.lotto.domain.player;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "player_profiles")
@Builder
@Getter
public class PlayerProfile {
    @Id
    private UUID id;
    private UUID playerId;
    private Integer totalGamesPlayed;
    private Integer totalWins;
    private String description;
    private String imageUrl;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;
}

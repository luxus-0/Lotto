package pl.lotto.domain.player;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "players")
@Builder
@Getter
@Setter
public class Player {
    @Id
    private UUID id;
    @NotBlank(message = "Username cannot be blank")
    private String username;
    @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$", message = "Invalid phone number")
    private String phone;
    @Email(message = "Invalid email")
    private String email;
    private String password;
    private boolean active;

    public Player(String username, String phone, String email, boolean active) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.active = active;
    }
}

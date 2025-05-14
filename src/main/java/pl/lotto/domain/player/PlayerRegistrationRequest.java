package pl.lotto.domain.player;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PlayerRegistrationRequest(
        @NotBlank String username,
        @NotBlank @Email(message = "Invalid email") String email,
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#!^&*])[A-Za-z\\d@#!^&*]{8,}$",
                message = "Password must contain at least one uppercase letter, one digit," +
                        " one special character (@#!^&*), and be at least 8 characters long.")
        String password,
        @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$", message = "Invalid phone number")
        @NotBlank String phone) {
}

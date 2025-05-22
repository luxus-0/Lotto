package pl.lotto.infrastructure.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.time.*;

@AllArgsConstructor
@Component
public class TokenAuthenticatorFacade {
    private final AuthenticationManager authenticationManager;
    private final TokenConfigurationProperties properties;
    private final Clock clock;

    public TokenResponseDto authenticateAndGenerateToken(TokenRequestDto tokenRequestDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(tokenRequestDto.username(), tokenRequestDto.password()));
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();
        String token = generateToken(user);

        return TokenResponseDto.builder()
                .username(username)
                .token(token)
                .build();
    }

    private String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(properties.getSecretKey());

        Instant expiresAt = LocalDateTime.now(clock).toInstant(ZoneOffset.UTC)
                .plus(Duration.ofDays(properties.getExpirationDays()));

        String subject = user.getUsername();
        return JWT.create()
                .withExpiresAt(expiresAt)
                .withSubject(subject)
                .sign(algorithm);
    }
}

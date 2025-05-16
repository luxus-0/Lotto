package pl.lotto.infrastructure.security;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final JwtConfigurationProperties properties;

    public JwtService(JwtConfigurationProperties properties) {
        this.properties = properties;
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + properties.getExpirationMs());

        SecretKey secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(properties.getSecret()));

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer(username)
                .subject(properties.getSubject())
                .issuer(properties.getIssuer())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }
}


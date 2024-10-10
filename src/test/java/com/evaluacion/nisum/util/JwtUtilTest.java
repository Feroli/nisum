package com.evaluacion.nisum.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;

public class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil();
    private final String USERNAME = "testuser";

    @Test
    @DisplayName("Debería generar un token JWT válido para un username")
    void shouldGenerateValidToken() {
        String token = jwtUtil.generateToken(USERNAME);

        assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("Debería generar un token JWT con los claims correctos")
    void shouldGenerateTokenWithCorrectClaims() {
        long expirationTime = 864_000_000;

        String token = jwtUtil.generateToken(USERNAME);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtUtil.getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertThat(claims.getSubject()).isEqualTo(USERNAME);
        assertThat(claims.getExpiration()).isAfter(new Date());
        assertThat(claims.getExpiration().getTime() - System.currentTimeMillis())
                .isLessThanOrEqualTo(expirationTime);
    }

    @Test
    @DisplayName("Debería generar un token con expiración futura")
    void shouldGenerateTokenWithFutureExpiration() {
        String token = jwtUtil.generateToken(USERNAME);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtUtil.getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertThat(claims.getExpiration()).isAfter(new Date());
    }

    @Test
    @DisplayName("Debería obtener la clave de firma correcta")
    void shouldGetCorrectSigningKey() {
        String secretKey = "TuClaveSecretaParaJWT12345678901234";

        Key key = jwtUtil.getSigningKey();

        assertThat(key).isNotNull();
        assertThat(key.getAlgorithm()).isEqualTo(Keys.hmacShaKeyFor(secretKey.getBytes()).getAlgorithm());
    }
}
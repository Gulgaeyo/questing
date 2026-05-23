package com.app.questing.config;

import com.app.questing.dto.user.UserDTO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JwtProvider {

    private final Algorithm algorithm;
    private final long accessTokenExpirationMinutes;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration-minutes}") long accessTokenExpirationMinutes
    ) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.accessTokenExpirationMinutes = accessTokenExpirationMinutes;
    }

    public String createAccessToken(UserDTO user) {
        Instant now = Instant.now();

        return JWT.create()
                .withSubject(String.valueOf(user.getId()))
                .withClaim("loginId", user.getLoginId())
                .withClaim("nickName", user.getNickName())
                .withIssuedAt(now)
                .withExpiresAt(now.plusSeconds(accessTokenExpirationMinutes * 60))
                .sign(algorithm);
    }

    public Long getUserId(String token) {
        return Long.valueOf(
                JWT.require(algorithm)
                        .build()
                        .verify(token)
                        .getSubject()
        );
    }
}

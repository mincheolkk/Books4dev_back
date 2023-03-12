package com.project.book.common.config.jwt;

import com.project.book.member.domain.Token;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;


import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${security.oauth2.resource.jwt.key-value}")
    private String secretKey;

    public static long ACCESS_TOKEN_VALID_TIME = 60 * 60 * 1000L;
    public static long REFRESH_TOKEN_VALID_TIME = 24 * 60 * 60 * 1000L;

    public Token createToken(final String payload, final long time) {
        Claims claims = Jwts.claims().setSubject(payload);
        String value = createTokenValue(claims, time);

        return new Token(value, time);
    }

    public String createTokenValue(final Claims claims, final long time) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + time);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Claims getPayload(final String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(final String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());

        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthenticationCredentialsNotFoundException("유효하지 않는 토큰입니다.");
        }
    }
}

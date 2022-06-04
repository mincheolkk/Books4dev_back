package com.project.book.common.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;


import java.util.Date;

@Component
public class JwtTokenProvider {

//    @Value("${security.oauth2.resource.jwt.key-value}")
    private static String secretKey = "sdsfadsfewqrgewqrgavsyhyjmyfur5tym5346234gbbh3e4r5fq324453254";

    private static long ACCESS_TOKEN_VALID_TIME = 60 * 60 * 1000L;
    private static long REFRESH_TOKEN_VALID_TIME = 24 * 60 * 60 * 1000L;

    public String createAccessToken(String payload) {
        System.out.println("in JwtTokenProvider on createToken");

        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME);

        System.out.println("secretKey = " + secretKey);
        System.out.println("validity = " + validity);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken(String value) {
        Claims claims = Jwts.claims();
        claims.put("value", value);
        Date now = new Date();
        Date validity = new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    public String getPayload(String token) {
        System.out.println("in JwtTokenProvider on getPayload");

        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        System.out.println("in JwtTokenProvider on validateToken");

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

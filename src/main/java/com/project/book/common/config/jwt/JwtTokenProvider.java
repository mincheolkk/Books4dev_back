package com.project.book.common.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;


import java.util.Date;
import java.util.LinkedList;

@Component
public class JwtTokenProvider {

//    @Value("${security.oauth2.resource.jwt.key-value}")
    private static String secretKey = "sdsfadsfewqrgewqrgavsyhyjmyfur5tym5346234gbbh3e4r5fq324453254";

//    @Value("${security.oauth2.resource.jwt.token-validity}")
    private static long validityInMilliseconds = 8640000;

    public String createToken(String payload) {
        System.out.println("secretKey = " + secretKey);
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        System.out.println("validity = " + validity);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getPayload(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

package com.project.book.member.service;

import com.project.book.common.config.jwt.AuthorizationExtractor;
import com.project.book.common.config.jwt.JwtTokenProvider;
import com.project.book.common.config.jwt.LoginMember;
import com.project.book.common.config.jwt.RedisUtil;
import com.project.book.common.exception.InvalidRefreshTokenException;
import com.project.book.member.domain.Member;
import com.project.book.member.domain.MemberType;
import com.project.book.member.domain.Token;
import com.project.book.member.dto.TokenRequest;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;
import java.util.UUID;

import static com.project.book.common.config.jwt.JwtTokenProvider.ACCESS_TOKEN_VALID_TIME;
import static com.project.book.common.config.jwt.JwtTokenProvider.REFRESH_TOKEN_VALID_TIME;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    private final AuthorizationExtractor authExtractor;


    @Transactional
    public String updateAccessToken(TokenRequest tokenRequest, Member member) {
        String oAuth = member.getOAuth();

        if (!jwtTokenProvider.validateToken(tokenRequest.getRefreshToken())) {
            throw new InvalidRefreshTokenException();
        }
        String refreshToken = redisUtil.getRefreshTokenData(oAuth);

        if (!refreshToken.equals(tokenRequest.getRefreshToken())) {
            throw new InvalidRefreshTokenException();
        }
        return jwtTokenProvider.createToken(oAuth, ACCESS_TOKEN_VALID_TIME).getValue();
    }

    @Transactional
    public void logOut(HttpServletRequest request) {
        String accessToken = authExtractor.extract(request);
        redisUtil.setBlackList(accessToken,"blackList",ACCESS_TOKEN_VALID_TIME);
    }

    //
    @Transactional
    public ResponseEntity getRefresh(Member member) {
        String oAuth = member.getOAuth();
        String randomValue = UUID.randomUUID().toString();
        Token refreshToken = jwtTokenProvider.createToken(randomValue, REFRESH_TOKEN_VALID_TIME);

        redisUtil.setRefreshToken(oAuth, refreshToken.getValue(), refreshToken.getExpiredTime());
        return new ResponseEntity(refreshToken.getValue(), HttpStatus.OK);
    }





//    public void checkToken(HttpServletRequest request) {
//        String token = authExtractor.extract(request);
//        Claims payload = jwtTokenProvider.getPayload(token);
//        long expiration = payload.getExpiration().getTime();
//        long now = new Date().getTime();
//
//        HttpServletResponse response = new HttpServletResponse();
//        if (expiration - now < 150 * 1000) {
//
//        }
//
//    }



}

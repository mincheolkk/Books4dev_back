package com.project.book.member.service;

import com.project.book.common.config.jwt.AuthorizationExtractor;
import com.project.book.common.config.jwt.JwtTokenProvider;
import com.project.book.common.config.jwt.RedisUtil;
import com.project.book.common.exception.InvalidRefreshTokenException;
import com.project.book.member.dto.TokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import static com.project.book.common.config.jwt.JwtTokenProvider.ACCESS_TOKEN_VALID_TIME;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    @Transactional
    public String updateAccessToken(TokenRequest tokenRequest, HttpServletRequest request) {
        String oAuth = (String) request.getAttribute("oAuth");

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
        String oAuth = (String) request.getAttribute("oAuth");
        String accessToken = AuthorizationExtractor.extract(request);
        System.out.println("logOut == ");
        System.out.println("accessToken = " + accessToken);

        redisUtil.setBlackList(accessToken,"blackList",ACCESS_TOKEN_VALID_TIME);

    }
}

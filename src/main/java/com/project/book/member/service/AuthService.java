package com.project.book.member.service;

import com.project.book.common.config.jwt.JwtTokenProvider;
import com.project.book.common.exception.InvalidRefreshTokenException;
import com.project.book.member.dto.TokenRequest;
import com.project.book.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;


    @Transactional
    public String updateAccessToken(String accessToken, TokenRequest refreshTokenRequest) {
        if (!jwtTokenProvider.validateToken(refreshTokenRequest.getRefreshToken())) {
            throw new InvalidRefreshTokenException();
        }

        String id = jwtTokenProvider.getPayload(accessToken);
        String refreshToken = memberRepository.findByoAuth(id).getRefreshToken();

        if (!refreshToken.equals(refreshTokenRequest.getRefreshToken())) {
            throw new InvalidRefreshTokenException();
        }

        return jwtTokenProvider.createAccessToken(id);
    }

//    public void logOut(String accessToken)
}

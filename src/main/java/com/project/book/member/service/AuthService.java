package com.project.book.member.service;

import com.project.book.common.config.jwt.AuthorizationExtractor;
import com.project.book.common.config.jwt.JwtTokenProvider;
import com.project.book.common.utils.RedisUtil;
import com.project.book.common.exception.InvalidRefreshTokenException;
import com.project.book.member.domain.Member;
import com.project.book.member.domain.Token;
import com.project.book.member.dto.request.TokenRequest;
import com.project.book.member.dto.response.MemberResponse;
import com.project.book.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.util.UUID;

import static com.project.book.common.config.jwt.JwtTokenProvider.ACCESS_TOKEN_VALID_TIME;
import static com.project.book.common.config.jwt.JwtTokenProvider.REFRESH_TOKEN_VALID_TIME;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;
    private final AuthorizationExtractor authExtractor;
    private final MemberRepository memberRepository;

    private static final String BLACK_LIST = "blackList";

    public String updateAccessToken(final String oAuth, final TokenRequest tokenRequest) {
        if (!jwtTokenProvider.validateToken(tokenRequest.getRefreshToken())) {
            throw new InvalidRefreshTokenException();
        }

        String refreshToken = redisUtil.getRefreshToken(oAuth);

        if (!refreshToken.equals(tokenRequest.getRefreshToken())) {
            throw new InvalidRefreshTokenException();
        }
        return jwtTokenProvider.createToken(oAuth, ACCESS_TOKEN_VALID_TIME).getValue();
    }

    public void logOut(final HttpServletRequest request, final String oAuth) {
        String accessToken = authExtractor.extract(request);
        redisUtil.setBlackList(accessToken, BLACK_LIST, ACCESS_TOKEN_VALID_TIME);
        redisUtil.deleteRefreshToken(oAuth);
    }

    public ResponseEntity getRefresh(final String oAuth) {
        String randomValue = UUID.randomUUID().toString();
        Token refreshToken = jwtTokenProvider.createToken(randomValue, REFRESH_TOKEN_VALID_TIME);

        redisUtil.setRefreshToken(oAuth, refreshToken.getValue(), refreshToken.getExpiredTime());
        return new ResponseEntity(refreshToken.getValue(), HttpStatus.OK);
    }

    public ResponseEntity<MemberResponse> getMyProfile(final String oAuth) {
        Member member = memberRepository.findByoAuth(oAuth);
        return ResponseEntity.ok(MemberResponse.from(member));
    }
}

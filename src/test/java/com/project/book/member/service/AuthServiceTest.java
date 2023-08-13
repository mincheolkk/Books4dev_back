package com.project.book.member.service;

import com.project.book.common.config.jwt.JwtTokenProvider;
import com.project.book.common.exception.InvalidRefreshTokenException;
import com.project.book.member.domain.Member;
import com.project.book.member.domain.MemberType;
import com.project.book.member.domain.Nickname;
import com.project.book.member.domain.Token;
import com.project.book.member.dto.request.TokenRequest;
import com.project.book.member.dto.response.MemberResponse;
import com.project.book.member.dto.response.TokenResponse;
import com.project.book.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static com.project.book.common.config.jwt.JwtTokenProvider.ACCESS_TOKEN_VALID_TIME;
import static com.project.book.common.config.jwt.JwtTokenProvider.REFRESH_TOKEN_VALID_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private TokenService tokenService;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private AuthService authService;

    @DisplayName("refresh 토큰의 검증이 실패하면 예외가 발생한다.")
    @Test
    void updateAccessTokenButFail() {
        // given
        Member member = Member.builder()
                .oAuth("123")
                .build();

        String randomString = UUID.randomUUID().toString();
        Token refreshToken = new Token(randomString, 1000L);
        TokenRequest tokenRequest = new TokenRequest(refreshToken.getValue());

        given(jwtTokenProvider.validateToken(tokenRequest.getRefreshToken())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.updateAccessToken(member.getOAuth(), tokenRequest))
                .isInstanceOf(InvalidRefreshTokenException.class)
                .hasMessage("유효하지 않은 Refresh 토큰입니다.");
    }

    @DisplayName("저장된 refresh 토큰과 tokenRequest의 refresh 토큰이 다르면 예외가 발생한다.")
    @Test
    void updateAccessTokenButFail2() {
        // given
        Member member = Member.builder()
                .oAuth("123")
                .build();

        String randomString = UUID.randomUUID().toString();
        Token refreshToken = new Token(randomString, 1000L);
        TokenRequest tokenRequest = new TokenRequest(refreshToken.getValue());

        String notEqualRandomString = randomString + "!@#$%^&*";

        given(jwtTokenProvider.validateToken(tokenRequest.getRefreshToken())).willReturn(true);
        given(tokenService.getRefreshToken(member.getOAuth())).willReturn(notEqualRandomString);

        // when & then
        assertThatThrownBy(() -> authService.updateAccessToken(member.getOAuth(), tokenRequest))
                .isInstanceOf(InvalidRefreshTokenException.class)
                .hasMessage("유효하지 않은 Refresh 토큰입니다.");
    }

    @DisplayName("refresh 토큰의 검증이 성공하면, access 토큰을 재발급한다.")
    @Test
    void updateAccessToken() {
        // given
        Member member = Member.builder()
                .oAuth("123")
                .build();

        String randomString = UUID.randomUUID().toString();
        Token refreshToken = new Token(randomString, 1000L);
        TokenRequest tokenRequest = new TokenRequest(refreshToken.getValue());
        String newAccessToken = "newAccessToken";

        given(jwtTokenProvider.validateToken(tokenRequest.getRefreshToken())).willReturn(true);
        given(tokenService.getRefreshToken(member.getOAuth())).willReturn(randomString);
        given(jwtTokenProvider.createToken(member.getOAuth(), ACCESS_TOKEN_VALID_TIME))
                .willReturn(new Token(newAccessToken, ACCESS_TOKEN_VALID_TIME));

        // when
        TokenResponse tokenResponse = authService.updateAccessToken(member.getOAuth(), tokenRequest);

        // then
        assertThat(tokenResponse.getAccessToken()).isEqualTo(newAccessToken);
    }

    @DisplayName("refresh토큰을 생성하고 tokenService에 넘겨준다.")
    @Test
    void createRefreshToken() {
        // given
        Member member = Member.builder()
                .oAuth("123")
                .build();

        String randomString = UUID.randomUUID().toString();
        Token refreshToken = new Token(randomString, REFRESH_TOKEN_VALID_TIME);

        given(jwtTokenProvider.createToken(anyString(), eq(REFRESH_TOKEN_VALID_TIME))).willReturn(refreshToken);

        // when
        TokenResponse response = authService.createRefreshToken(member.getOAuth());

        // then
        assertThat(response.getAccessToken()).isEqualTo(refreshToken.getValue());
        verify(jwtTokenProvider, times(1)).createToken(anyString(), eq(REFRESH_TOKEN_VALID_TIME));
        verify(tokenService, times(1)).setRefreshToken(member.getOAuth(), refreshToken.getValue(), refreshToken.getExpiredTime());
    }

    @DisplayName("유저의 oAuth를 통해서 해당 유저의 프로필을 반환한다.")
    @Test
    void getMyProfile() {
        // given
        Member member = Member.builder()
                .oAuth("123")
                .nickname(new Nickname("mincheol"))
                .type(MemberType.BackEnd)
                .build();

        given(memberRepository.findByoAuth(member.getOAuth())).willReturn(member);

        // when
        MemberResponse memberResponse = authService.getMyProfile(member.getOAuth());

        // then
        assertAll(
                () -> {
                    assertThat(memberResponse.getOAuth()).isEqualTo(member.getOAuth());
                    assertThat(memberResponse.getNickname()).isEqualTo(member.getNickname().getNickname());
                    assertThat(memberResponse.getMemberType()).isEqualTo(member.getType());
                }
        );
    }
}
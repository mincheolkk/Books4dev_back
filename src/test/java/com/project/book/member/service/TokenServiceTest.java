package com.project.book.member.service;

import com.project.book.member.domain.Member;
import com.project.book.member.domain.Token;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private StringRedisTemplate loginTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private TokenService tokenService;

    @DisplayName("refreshToken을 Redis에 저장한다.")
    @Test
    void setRefreshToken() {
        // given
        Member member = Member.builder()
                .oAuth("123")
                .build();

        String randonValue = UUID.randomUUID().toString();
        Token refreshToken = new Token(randonValue, 1000L);

        given(loginTemplate.opsForValue()).willReturn(valueOperations);

        // when
        tokenService.setRefreshToken(member.getOAuth(), refreshToken.getValue(), refreshToken.getExpiredTime());

        // then
        verify(valueOperations, times(1)).set(member.getOAuth(), refreshToken.getValue(), Duration.ofMillis(refreshToken.getExpiredTime()));
    }

    @DisplayName("refreshToken을 Redis에서 삭제한다.")
    @Test
    void deleteRefreshToken() {
        // given
        Member member = Member.builder()
                .oAuth("123")
                .build();

        // when
        tokenService.deleteRefreshToken(member.getOAuth());

        // then
        verify(loginTemplate, times(1)).delete(member.getOAuth());
    }

    @DisplayName("저장된 refreshToken을 가져온다.")
    @Test
    void getRefreshToken() {
        // given
        Member member = Member.builder()
                .oAuth("123")
                .build();

        String randonValue = UUID.randomUUID().toString();
        Token refreshToken = new Token(randonValue, 1000L);

        given(loginTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get(member.getOAuth())).willReturn(refreshToken.getValue());

        // when
        String result = tokenService.getRefreshToken(member.getOAuth());

        // then
        verify(valueOperations, times(1)).get(member.getOAuth());
        assertThat(result).isEqualTo(refreshToken.getValue());
    }

    @DisplayName("accessToken을 블랙리스트에 저장한다.")
    @Test
    void setBlackList() {
        // given
        String blackList = "blackList";
        Token accessToken = new Token("accessToken", 500L);

        given(loginTemplate.opsForValue()).willReturn(valueOperations);

        // when
        tokenService.setBlackList(accessToken.getValue(), blackList, 500L);

        // then
        verify(valueOperations, times(1)).set(accessToken.getValue(), blackList, Duration.ofMillis(accessToken.getExpiredTime()));
    }

    @DisplayName("accessToken이 블랙리스트에 존재하는지 확인한다.")
    @Test
    void getBlackList() {
        // given
        String blackList = "blackList";
        Token accessToken = new Token("accessToken", 500L);

        given(loginTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get(accessToken.getValue())).willReturn(blackList);

        // when
        String result = tokenService.getBlackList(accessToken.getValue());

        // then
        verify(valueOperations, times(1)).get(accessToken.getValue());
        assertThat(result).isEqualTo(blackList);
    }
}
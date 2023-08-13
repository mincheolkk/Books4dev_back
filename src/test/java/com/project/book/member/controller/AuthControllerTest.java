package com.project.book.member.controller;

import com.project.book.member.domain.Member;
import com.project.book.member.domain.Nickname;
import com.project.book.member.dto.request.TokenRequest;
import com.project.book.member.dto.response.MemberResponse;
import com.project.book.member.dto.response.TokenResponse;
import com.project.book.support.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class AuthControllerTest extends ControllerTest {

    @DisplayName("멤버 본인의 프로필을 조회한다.")
    @Test
    void getMyProfile() throws Exception {
        // given
        String oAuth = "123";
        String nickname = "test";
        Member member = Member.builder()
                .oAuth(oAuth)
                .nickname(new Nickname(nickname))
                .build();
        MemberResponse memberResponse = MemberResponse.from(member);

        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getPayload(anyString())).willReturn(oAuth);
        given(authService.getMyProfile(anyString())).willReturn(memberResponse);

        // when & then
        mockMvc.perform(
                        get("/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(nickname))
                .andExpect(jsonPath("$.oauth").value(oAuth));

    }

    @DisplayName("refresh 토큰으로 새로운 access 토큰을 발급한다.")
    @Test
    void updateToken() throws Exception {
        // given
        String oAuth = "123";
        String refreshToken = "refreshToken";
        TokenRequest tokenRequest = new TokenRequest(refreshToken);

        String newAccessToken = "accessToken";
        TokenResponse tokenResponse = new TokenResponse(newAccessToken);

        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getPayload(anyString())).willReturn(oAuth);
        given(authService.updateAccessToken(anyString(), any(TokenRequest.class))).willReturn(tokenResponse);

        // when & then
        mockMvc.perform(
                        post("/auth/update/token")
                                .content(objectMapper.writeValueAsString(tokenRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(newAccessToken));
    }

    @DisplayName("로그아웃 한다.")
    @Test
    void logOut() throws Exception {
        // given
        String oAuth = "123";
        String accessToken = "accessToken";
        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getPayload(anyString())).willReturn(oAuth);

        given(authExtractor.extract(any(HttpServletRequest.class))).willReturn(accessToken);

        willDoNothing().given(authService).logOut(any(HttpServletRequest.class), anyString());

        // when & then
        mockMvc.perform(
                        get("/auth/out")
                )
                .andExpect(status().isNoContent());
    }

    @DisplayName("refresh 토큰을 발급한다.")
    @Test
    void createRefreshToken() throws Exception {
        // given
        String oAuth = "123";
        String refreshToken = "refreshToken";
        TokenResponse tokenResponse = new TokenResponse(refreshToken);

        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getPayload(anyString())).willReturn(oAuth);

        given(authService.createRefreshToken(anyString())).willReturn(tokenResponse);

        // when & then

        mockMvc.perform(
                        get("/auth/refreshtoken"))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.nickname").value(nickname))


    }

}
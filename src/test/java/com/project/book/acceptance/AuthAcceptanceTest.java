package com.project.book.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;


public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("로그인 요청이 카카오 인증 페이지로 리다이렉트 되는지 확인한다")
    @Test
    void kakaoLoginThenRedirect() {
        // given
        String loginRequestUrl = "/oauth2/authorization/kakao";
        String kakaoOAuthUrl = "https://kauth.kakao.com/oauth/authorize";

        // when
        ExtractableResponse<Response> response =  RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post(loginRequestUrl)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FOUND.value());
        assertThat(response.header("Location")).startsWith(kakaoOAuthUrl);
    }
}

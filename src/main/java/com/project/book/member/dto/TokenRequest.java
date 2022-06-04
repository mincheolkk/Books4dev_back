package com.project.book.member.dto;

import lombok.Getter;

@Getter
public class TokenRequest {

    private String refreshToken;

    public TokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

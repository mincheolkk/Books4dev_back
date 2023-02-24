package com.project.book.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class TokenResponse {

    private String accessToken;

    public TokenResponse(final String accessToken) {
        this.accessToken = accessToken;
    }
}

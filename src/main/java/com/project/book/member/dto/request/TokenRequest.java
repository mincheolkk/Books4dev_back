package com.project.book.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class TokenRequest {

    private String refreshToken;

    public TokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

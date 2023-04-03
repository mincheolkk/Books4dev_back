package com.project.book.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginMemberDto {

    private String oAuth;

    public LoginMemberDto(final String oAuth) {
        this.oAuth = oAuth;
    }
}

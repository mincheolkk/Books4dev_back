package com.project.book.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class TokenRequest {

    private String refreshToken;

}

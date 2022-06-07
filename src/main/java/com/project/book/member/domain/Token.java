package com.project.book.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class Token {
    private String value;
    private long expiredTime;

    public Token(String value, long expiredTime) {
        this.value = value;
        this.expiredTime = expiredTime;
    }
}


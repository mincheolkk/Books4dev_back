package com.project.book.exception;

public class InvalidTokenException extends RuntimeException {

    private static final String INVALID_TOKEN = "올바르지 않은 토큰 입니다.";

    public InvalidTokenException() {
        super(INVALID_TOKEN);
    }
}


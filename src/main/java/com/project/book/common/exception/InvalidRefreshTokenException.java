package com.project.book.common.exception;

public class InvalidRefreshTokenException extends RuntimeException{

    private static final String MESSAGE = "유효하지 않은 Refresh 토큰입니다.";

    public InvalidRefreshTokenException() {
        super(MESSAGE);
    }
}

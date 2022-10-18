package com.project.book.common.exception;

public class InvalidAccessTokenException extends RuntimeException{

    private static final String MESSAGE = "유효하지 않은 Access 토큰입니다.";

    public InvalidAccessTokenException() {
        super(MESSAGE);
    }
}

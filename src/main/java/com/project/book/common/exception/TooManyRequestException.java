package com.project.book.common.exception;

public class TooManyRequestException extends RuntimeException {

    private static final String MESSAGE = "짧은 시간에 너무 많은 요청을 보냈습니다.";

    public TooManyRequestException() {
        super(MESSAGE);
    }
}
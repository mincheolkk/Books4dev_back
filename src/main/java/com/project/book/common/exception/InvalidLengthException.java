package com.project.book.common.exception;

public class InvalidLengthException extends RuntimeException{

    private static final String MESSAGE = "유효하지 길이 입니다.";

    public InvalidLengthException() {
        super(MESSAGE);
    }
}

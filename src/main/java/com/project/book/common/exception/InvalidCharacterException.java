package com.project.book.common.exception;

public class InvalidCharacterException extends RuntimeException{

    private static final String MESSAGE = "유효하지 않은 문자가 있습니다.";

    public InvalidCharacterException() {
        super(MESSAGE);
    }
}

package com.project.book.common.exception;

public class InvalidOwnerException extends RuntimeException{

    private static final String MESSAGE = "유효하지 않은 사용자의 접근입니다.";

    public InvalidOwnerException() {
        super(MESSAGE);
    }
}

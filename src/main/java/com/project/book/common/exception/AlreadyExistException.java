package com.project.book.common.exception;

public class AlreadyExistException  extends RuntimeException {

    private static final String MESSAGE = "이미 존재합니다.";

    public AlreadyExistException() {
        super(MESSAGE);
    }
}

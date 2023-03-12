package com.project.book.common.exception;

public class ContentNotFoundException extends RuntimeException {

    private static final String MESSAGE = "찾을 수 없습니다.";

    public ContentNotFoundException() {
        super(MESSAGE);
    }
}

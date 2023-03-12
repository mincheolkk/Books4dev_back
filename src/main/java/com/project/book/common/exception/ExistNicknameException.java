package com.project.book.common.exception;

public class ExistNicknameException extends RuntimeException {

    private static final String MESSAGE = "이미 존재하는 닉네임입니다.";

    public ExistNicknameException() {
        super(MESSAGE);
    }
}

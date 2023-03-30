package com.project.book.member.domain;

import com.project.book.common.exception.InvalidCharacterException;
import com.project.book.common.exception.InvalidLengthException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.regex.Pattern;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Nickname {

    private static final Pattern NICKNAME_PATTERN = Pattern
            .compile("^[가-힣a-zA-Z0-9_-]+$");
    private static final int MAX_NICKNAME_LENGTH = 10;

    private String nickname;

    public Nickname(String nickname) {
        validate(nickname);
        this.nickname = nickname;
    }

    public void validate(String nickname) {
        if (nickname.isBlank() || nickname.length() > MAX_NICKNAME_LENGTH) {
            throw new InvalidLengthException();
        }

        if (!NICKNAME_PATTERN.matcher(nickname).matches()) {
            throw new InvalidCharacterException();
        }
    }
}

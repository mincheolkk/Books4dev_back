package com.project.book.member.domain;

import com.project.book.common.exception.InvalidCharacterException;
import com.project.book.common.exception.InvalidLengthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

public class NicknameTest {

    @DisplayName("가능한 닉네임 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"1", "광진구화양동", "ab_1", "가가가나나나다다다라"})
    void success(String nickname) {
        assertThatNoException().isThrownBy(() -> new Nickname(nickname));
    }

    @DisplayName("잘못된 닉네임 길이 테스트")
    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"   ", "가가가나나나다다다라라"})
    void fail_length(String nickname) {
        assertThatThrownBy(() -> new Nickname(nickname))
                .isInstanceOf(InvalidLengthException.class);
    }

    @DisplayName("잘못된 문자들 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"공백  불가", "특수문자불가!!", "자음불가ㄱㄱ","모음불가ㅠㅠ","점불가.","콤마불가,"})
    void fail_character(String nickname) {
        assertThatThrownBy(() -> new Nickname(nickname))
                .isInstanceOf(InvalidCharacterException.class);
    }
}



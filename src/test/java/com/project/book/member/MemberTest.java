package com.project.book.member;

import com.project.book.member.domain.Member;
import com.project.book.member.domain.Nickname;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.project.book.member.domain.MemberType.*;
import static org.assertj.core.api.Assertions.*;


public class MemberTest {

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .id(1L)
                .nickname(new Nickname("KIM"))
                .oAuth("12345678")
                .email(null)
                .type(BackEnd)
                .build();
    }

    @DisplayName("멤버의 직군을 바꾼다")
    @Test
    void change_position() {
        // when
        member.updateMemberPosition(FrontEnd);

        // then
        assertThat(member.getType()).isEqualTo(FrontEnd);
        assertThat(member.getType()).isNotEqualTo("FrontEnd");
    }

    @DisplayName("멤버의 닉네임을 바꾼다")
    @Test
    void change_nickname() {
        // given
        Nickname nickname = new Nickname("mincheol");

        // when
        member.updateMemberNickname(nickname);

        // then
        assertThat(member.getNickname()).isEqualTo(nickname);
    }

}

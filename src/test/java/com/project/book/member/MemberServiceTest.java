package com.project.book.member;

import com.project.book.member.domain.Member;
import com.project.book.member.repository.MemberRepository;
import com.project.book.member.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.project.book.member.domain.MemberType.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .oAuth("12")
                .type(iOS)
                .build();

        memberRepository.save(member);
    }

    @DisplayName("유저의 정보를 가져온다")
    @Test
    void getInfo() {
        assertAll(
                () -> {
                    assertThat(memberRepository.findByoAuth("12").getOAuth()).isEqualTo("12");
                    assertThat(memberRepository.findByoAuth("12").getType()).isEqualTo(iOS);
                }
        );

    }

    @DisplayName("유저의 직군을 변경한다")
    @Test
    void changePosition() {
        Member member = memberRepository.findByoAuth("12");
        // 도메인 객체에서 변경
        member.updateMemberPosition(Android);

        assertThat(member.getType()).isEqualTo(Android);
        assertThat(member.getType()).isNotEqualTo(iOS);

        // 서비스 레이어에서 변경
        memberService.addPosition(member, DATA);
        assertThat(member.getType()).isEqualTo(DATA);

    }

    @AfterEach
    void tearDown() {
        memberRepository.delete(member);
    }
}

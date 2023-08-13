package com.project.book.member.domain;

import com.project.book.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("OAuth를 통해 회원을 찾는다.")
    @Test
    void findByOAuth() {
        // given
        Member member = Member.builder()
                .oAuth("123")
                .build();

        memberRepository.save(member);

        // when
        Member savedMember = memberRepository.findByoAuth("123");

        // then
        assertThat(savedMember).isSameAs(member);
        assertThat(savedMember.getId()).isEqualTo(member.getId());
    }

    @DisplayName("특정 OAuth를 가지는 회원이 존재하는지 확인한다.")
    @Test
    void existsByOAuth() {
        // given
        Member member = Member.builder()
                .oAuth("11")
                .build();

        memberRepository.save(member);

        // when & then
        assertThat(memberRepository.existsByoAuth("11")).isTrue();
        assertThat(memberRepository.existsByoAuth("22")).isFalse();
    }

    @DisplayName("특정 NickName을 가지는 회원이 존재하는지 확인한다")
    @Test
    void existsByNickname() {
        // given
        Nickname nickname = new Nickname("mincheol");
        Nickname nickname2 = new Nickname("kim");

        Member member = Member.builder()
                .nickname(nickname)
                .build();

        memberRepository.save(member);

        // when & then
        assertThat(memberRepository.existsByNickname(nickname)).isTrue();
        assertThat(memberRepository.existsByNickname(nickname2)).isFalse();
    }
}

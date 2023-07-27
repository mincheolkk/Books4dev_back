package com.project.book.member.service;

import com.project.book.common.exception.ExistNicknameException;
import com.project.book.member.domain.Member;
import com.project.book.member.domain.MemberType;
import com.project.book.member.domain.Nickname;
import com.project.book.member.dto.request.NicknameRequest;
import com.project.book.member.dto.response.MemberResponse;
import com.project.book.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @DisplayName("유저가 직군을 선택했다면, HttpStatus를 OK로 보낸다.")
    @Test
    void hasPosition() {
        // given
        Member member = Member.builder()
                .oAuth("123")
                .type(MemberType.BackEnd)
                .build();
        memberRepository.save(member);

        // when
        MemberType memberType = memberService.hasPosition(member.getOAuth());

        // then
        assertThat(memberType).isEqualTo(member.getType());
    }

    @DisplayName("유저가 직군을 선택하지 않았다면, HttpStatus를 NO_CONTENT로 보낸다.")
    @Test
    void hasNoPosition() {
        // given
        Member member = Member.builder()
                .oAuth("123")
                .build();
        memberRepository.save(member);

        // when
        MemberType memberType = memberService.hasPosition(member.getOAuth());

        // then
        assertThat(memberType).isEqualTo(null);
    }

    @DisplayName("유저의 직군을 추가한다.")
    @Test
    void addPosition() {
        // given
        Member member = Member.builder()
                .oAuth("123")
                .build();
        memberRepository.save(member);

        // when
        memberService.addPosition(member.getOAuth(), MemberType.BackEnd);

        // then
        Member savedMember = memberRepository.findByoAuth("123");
        assertThat(savedMember.getType()).isEqualTo(MemberType.BackEnd);
    }

    @DisplayName("유저의 직군을 변경한다.")
    @Test
    void changePosition() {
        // given
        Member member = Member.builder()
                .oAuth("123")
                .type(MemberType.BackEnd)
                .build();
        memberRepository.save(member);

        // when
        memberService.addPosition(member.getOAuth(), MemberType.FrontEnd);

        // then
        Member savedMember = memberRepository.findByoAuth("123");
        assertThat(savedMember.getType()).isEqualTo(MemberType.FrontEnd);
    }

    @DisplayName("유저의 닉네임을 추가한다.")
    @Test
    void addNickName() {
        // given
        Member member = Member.builder()
                .oAuth("123")
                .type(MemberType.BackEnd)
                .build();
        memberRepository.save(member);

        // when
        String nickName = "mincheol";
        NicknameRequest nicknameRequest = new NicknameRequest(nickName);

        memberService.addNickname(member.getOAuth(), nicknameRequest);

        // then
        Member savedMember = memberRepository.findByoAuth("123");
        assertThat(savedMember.getNickname().getNickname()).isEqualTo(nickName);
    }

    @DisplayName("이미 존재하는 닉네임을 추가할 경우 예외가 발생한다.")
    @Test
    void addNickNameButFail() {
        // given
        String nickName = "mincheol";

        Member member = Member.builder()
                .oAuth("123")
                .type(MemberType.DevOps)
                .nickname(new Nickname(nickName))
                .build();
        memberRepository.save(member);

        Member newMember = Member.builder()
                .oAuth("12345")
                .type(MemberType.BackEnd)
                .build();
        memberRepository.save(newMember);

        // when & then
        NicknameRequest nicknameRequest = new NicknameRequest(nickName);

        assertThatThrownBy(() -> memberService.addNickname(newMember.getOAuth(), nicknameRequest))
                .isInstanceOf(ExistNicknameException.class)
                .hasMessage("이미 존재하는 닉네임입니다.");
    }

    @DisplayName("유저의 oAuth를 통해서 해당 유저의 프로필을 반환한다.")
    @Test
    void test() {
        // given
        String oAuth = "123";
        String nickName = "mincheol";

        Member member = Member.builder()
                .oAuth(oAuth)
                .type(MemberType.DevOps)
                .nickname(new Nickname(nickName))
                .build();
        memberRepository.save(member);

        // when
        MemberResponse memberResponse = memberService.getMemberProfile(Long.parseLong(oAuth));

        // then
        assertThat(memberResponse.getNickname()).isEqualTo(nickName);
        assertThat(memberResponse.getOAuth()).isEqualTo(oAuth);
    }
}
package com.project.book.member.service;

import com.project.book.common.exception.ExistNicknameException;
import com.project.book.member.domain.Member;
import com.project.book.member.domain.MemberType;
import com.project.book.member.domain.Nickname;
import com.project.book.member.dto.request.NicknameRequest;
import com.project.book.member.dto.response.MemberResponse;
import com.project.book.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberType hasPosition(final String oAuth) {
        Member member = memberRepository.findByoAuth(oAuth);

        MemberType type = member.getType();
        return type;
    }

    @Transactional
    public void addPosition(final String oAuth, final MemberType position) {
        Member member = memberRepository.findByoAuth(oAuth);
        member.updateMemberPosition(position);
    }

    @Transactional
    public void addNickname(final String oAuth, final NicknameRequest request) {
        Member member = memberRepository.findByoAuth(oAuth);

        Nickname nickname = new Nickname(request.getNickname());
        if (memberRepository.existsByNickname(nickname)) {
            throw new ExistNicknameException();
        }

        member.updateMemberNickname(nickname);
    }

    public MemberResponse getMemberProfile(final Long id) {
        String oauth = String.valueOf(id);
        Member member = memberRepository.findByoAuth(oauth);
        return MemberResponse.from(member);
    }
}

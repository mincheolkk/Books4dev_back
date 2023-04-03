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

    public ResponseEntity<?> hasPosition(final String oAuth) {
        Member member = memberRepository.findByoAuth(oAuth);

        MemberType type = member.getType();
        if (type == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> addPosition(final String oAuth, final MemberType position) {
        Member member = memberRepository.findByoAuth(oAuth);
        member.updateMemberPosition(position);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> addNickname(final String oAuth, final NicknameRequest request) {
        Member member = memberRepository.findByoAuth(oAuth);

        Nickname nickname = new Nickname(request.getNickname());
        if (memberRepository.existsByNickname(nickname)) {
            throw new ExistNicknameException();
        }

        member.updateMemberNickname(nickname);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> getMemberProfile(final Long id) {
        String oauth = String.valueOf(id);
        Member member = memberRepository.findByoAuth(oauth);
        return ResponseEntity.ok(MemberResponse.from(member));
    }
}

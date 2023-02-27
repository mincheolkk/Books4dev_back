package com.project.book.member.service;

import com.project.book.member.domain.Member;
import com.project.book.member.domain.MemberType;
import com.project.book.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public ResponseEntity<?> checkPosition(final Member member) {
        MemberType type = member.getType();
        if (type == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> addPosition(final Member member, final MemberType position) {
        member.updateMemberPosition(position);
        memberRepository.save(member);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

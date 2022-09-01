package com.project.book.member.service;

import com.project.book.member.domain.Member;
import com.project.book.member.domain.MemberType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    public ResponseEntity<?> checkPosition(Member member) {
        MemberType type = member.getType();
        if (type == null) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> addPosition(Member member, MemberType position) {
        member.updateMemberPosition(position);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
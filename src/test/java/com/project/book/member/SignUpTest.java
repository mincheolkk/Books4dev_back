package com.project.book.member;

import com.project.book.member.domain.Member;
import com.project.book.member.domain.MemberType;
import com.project.book.member.dto.CreateMemberRequest;
import com.project.book.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

@SpringBootTest
public class SignUpTest {
//
//    @Autowired
//    MemberRepository memberRepository;
//
//    @Test
//    @Commit
//    void 멤버데이터() {
//
//
//        memberRepository.save(
//                CreateMemberRequest.builder()
//                .type(MemberType.BACK)
//                .oAuth("2")
//                .nickname("백엔드 테스터")
//                .build()
//                        .toMember());
//
//        memberRepository.save(
//                CreateMemberRequest.builder()
//                        .type(MemberType.FRONT)
//                        .oAuth("3")
//                        .nickname("프론트 테스터")
//                        .build()
//                        .toMember());
//
//        memberRepository.save(
//                CreateMemberRequest.builder()
//                        .type(MemberType.FRONT)
//                        .oAuth("4")
//                        .nickname("잡다한 테스터")
//                        .build()
//                        .toMember());
//
//    }
}

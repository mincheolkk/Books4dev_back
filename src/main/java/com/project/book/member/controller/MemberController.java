package com.project.book.member.controller;

import com.project.book.common.config.jwt.LoginMember;
import com.project.book.member.domain.Member;
import com.project.book.member.domain.MemberType;
import com.project.book.member.dto.response.MemberResponse;
import com.project.book.member.dto.response.PositionResponseDto;
import com.project.book.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMember(@LoginMember final Member member) {
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @GetMapping(value = "/checkPosition")
    public ResponseEntity<?> checkPosition(@LoginMember final Member member) {
        return memberService.checkPosition(member);
    }

    @PostMapping("/selectPosition")
    public ResponseEntity<?> selectPosition(@LoginMember final Member member, @RequestBody final PositionResponseDto request) {
        return memberService.addPosition(member, request.getPosition());
    }
}

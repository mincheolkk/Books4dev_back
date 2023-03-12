package com.project.book.member.controller;

import com.project.book.book.service.BookService;
import com.project.book.common.config.jwt.LoginMember;
import com.project.book.member.domain.Member;
import com.project.book.member.domain.MemberType;
import com.project.book.member.dto.response.MemberResponse;
import com.project.book.member.dto.response.PositionResponseDto;
import com.project.book.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final BookService bookService;

    @GetMapping(value = "/checkPosition")
    public ResponseEntity<?> checkPosition(@LoginMember final Member member) {
        return memberService.checkPosition(member);
    }

    @PostMapping("/selectPosition")
    public ResponseEntity<?> selectPosition(@LoginMember final Member member, @RequestBody @Valid final PositionResponseDto request) {
        return memberService.addPosition(member, request.getPosition());
    }

    @GetMapping("/wish")
    public ResponseEntity<?> getAllWishBook(@LoginMember final Member member) {
        return bookService.getMyWishBook(member);
    }

    @GetMapping("/readBook")
    public ResponseEntity<?> getMyReadBook(@LoginMember final Member member) {
        return bookService.getMyReadBook(member);
    }
}

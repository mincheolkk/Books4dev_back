package com.project.book.member.controller;

import com.project.book.book.service.BookService;
import com.project.book.common.config.jwt.LoginMember;
import com.project.book.member.dto.LoginMemberDto;
import com.project.book.member.dto.request.NicknameRequest;
import com.project.book.member.dto.request.PositionRequest;
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
    public ResponseEntity<?> checkPosition(@LoginMember final LoginMemberDto loginMemberDto) {
        return memberService.hasPosition(loginMemberDto.getOAuth());
    }

    @PostMapping("/selectPosition")
    public ResponseEntity<?> selectPosition(@LoginMember final LoginMemberDto loginMemberDto, @RequestBody @Valid final PositionRequest request) {
        return memberService.addPosition(loginMemberDto.getOAuth(), request.getPosition());
    }

    @GetMapping("/{id}/wishBook")
    public ResponseEntity<?> getMemberWishBook(@PathVariable final Long id) {
        return bookService.getMemberWishBook(id);
    }

    @GetMapping("/{id}/readBook")
    public ResponseEntity<?> getMemberReadBook(@PathVariable final Long id) {
        return bookService.getMemberReadBook(id);
    }

    @PostMapping("/nickname")
    public ResponseEntity<?> saveNickname(@LoginMember final LoginMemberDto loginMemberDto, @RequestBody final NicknameRequest request) {
        return memberService.addNickname(loginMemberDto.getOAuth(), request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMemberProfile(@PathVariable Long id) {
        return memberService.getMemberProfile(id);
    }
}


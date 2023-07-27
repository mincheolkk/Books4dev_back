package com.project.book.member.controller;

import com.project.book.book.domain.BookTime;
import com.project.book.book.dto.response.ReadBookResponseDto;
import com.project.book.book.dto.response.WishBookResponseDto;
import com.project.book.book.service.BookService;
import com.project.book.book.service.ReadBookService;
import com.project.book.book.service.WishBookService;
import com.project.book.common.config.jwt.LoginMember;
import com.project.book.member.domain.MemberType;
import com.project.book.member.dto.LoginMemberDto;
import com.project.book.member.dto.request.NicknameRequest;
import com.project.book.member.dto.request.PositionRequest;
import com.project.book.member.dto.response.MemberResponse;
import com.project.book.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final BookService bookService;
    private final ReadBookService readBookService;
    private final WishBookService wishBookService;

    @GetMapping(value = "/checkPosition")
    public ResponseEntity<Void> checkPosition(@LoginMember final LoginMemberDto loginMemberDto) {
        MemberType type = memberService.hasPosition(loginMemberDto.getOAuth());
        if (type == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/selectPosition")
    public ResponseEntity<Void> selectPosition(@LoginMember final LoginMemberDto loginMemberDto, @RequestBody @Valid final PositionRequest request) {
        memberService.addPosition(loginMemberDto.getOAuth(), request.getPosition());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/wishBook")
    public ResponseEntity<List<WishBookResponseDto>> getMemberWishBook(@PathVariable final Long id) {
        List<WishBookResponseDto> memberWishBooks = wishBookService.getMemberWishBook(id);
        return ResponseEntity.ok(memberWishBooks);
    }

    @GetMapping("/{id}/readBook")
    public ResponseEntity<Map<BookTime, List<ReadBookResponseDto>>> getMemberReadBook(@PathVariable final Long id) {
        return ResponseEntity.ok(readBookService.getMemberReadBook(id));
    }

    @PostMapping("/nickname")
    public ResponseEntity<Void> saveNickname(@LoginMember final LoginMemberDto loginMemberDto, @RequestBody final NicknameRequest request) {
        memberService.addNickname(loginMemberDto.getOAuth(), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMemberProfile(@PathVariable Long id) {
        MemberResponse memberResponse = memberService.getMemberProfile(id);
        return ResponseEntity.ok(memberResponse);
    }
}


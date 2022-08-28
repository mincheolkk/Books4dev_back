package com.project.book.book.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.book.book.domain.Book;
import com.project.book.book.dto.request.AllBookFilterDto;
import com.project.book.book.dto.request.RegisterBySearchDto;
import com.project.book.book.dto.request.RegisterByHomeListDto;
import com.project.book.book.dto.request.WishBookRequestDto;
import com.project.book.book.dto.response.AllBookResponseDto;
import com.project.book.book.service.BookService;
import com.project.book.common.config.jwt.LoginMember;
import com.project.book.member.domain.Member;
import com.project.book.member.repository.MemberRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final MemberRepository memberRepository;

    // 책 저장 로직
    @PostMapping("/bySearch")
    public ResponseEntity registerBySearch(@LoginMember Member member, @RequestBody @Valid RegisterBySearchDto request) {
        Book book = bookService.registerBySearch(request, member);
        return new ResponseEntity<>(book.getId(), HttpStatus.OK);
    }

    @PostMapping("/bylist")
    public ResponseEntity registerByHomeList(@LoginMember Member member, @RequestBody @Valid RegisterByHomeListDto request) {
        Book book = bookService.registerByHomeList(request, member);

        return new ResponseEntity<>(book.getId(), HttpStatus.OK);

    }

    @GetMapping("/books/{book_id}")
    public ResponseEntity<Map<String, Object>> getDetailBook(@PathVariable("book_id") @Valid Long id) throws JsonProcessingException {
        Map<String, Object> detailBook = bookService.getDetailBook(id);

        return ResponseEntity.ok(detailBook);
    }

    @PostMapping("/book/wish")
    public ResponseEntity<?> saveWishBook(@LoginMember Member member, @RequestBody WishBookRequestDto request) {
        ResponseEntity responseEntity = bookService.saveWishBook(request, member);

        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }

    @GetMapping("/test/all")
    public ResponseEntity<?> getAllBooks(@ModelAttribute AllBookFilterDto condition) {
        PageRequest pageRequest = PageRequest.of(0, 100);
        ResponseEntity<?> result = bookService.getAllBook(condition, pageRequest);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/test/wish")
    public ResponseEntity<?> getAllWishBook(@LoginMember Member member) {
        return bookService.getAllWishBook(member);
    }

    @GetMapping("/test/readBook")
    public ResponseEntity<?> testReadBook(@LoginMember Member member) {
        return bookService.testReadBook(member);
    }


}

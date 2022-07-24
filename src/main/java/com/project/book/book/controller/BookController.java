package com.project.book.book.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.book.book.domain.Book;
import com.project.book.book.dto.request.BookRequestDto;
import com.project.book.book.dto.request.WishBookRequestDto;
import com.project.book.book.service.BookService;
import com.project.book.member.domain.Member;
import com.project.book.member.repository.MemberRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
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
    @PostMapping("/books")
    public ResponseEntity saveBook(@RequestBody @Valid BookRequestDto request) {
        Book book = bookService.createOrRegisterBook(request);
        URI uri = URI.create("/books/" + book.getIsbn());

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/books/{book_id}")
    public ResponseEntity<Map<String, Object>> getDetailBook(@PathVariable("book_id") @Valid Long id) throws JsonProcessingException {
        Map<String, Object> detailBook = bookService.getDetailBook(id);

        System.out.println("detailBook = " + detailBook.toString());
        return ResponseEntity.ok(bookService.getDetailBook(id));
    }

    //@LoginMember member 로 멤버도 함께 받기
    @PostMapping("/book/wish")
    public ResponseEntity<?> saveWishBook(@RequestBody WishBookRequestDto request) {
        System.out.println("request = " + request);
        System.out.println("request.getIsbn() = " + request.getIsbn());

        Optional<Member> tempMember = memberRepository.findById(1L);
        ResponseEntity responseEntity = bookService.saveWishBook(request, tempMember.get());

        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }


    @ResponseBody
    @GetMapping("/login/kakao")
    public void kakaoCallBack(@RequestParam String code) {
        System.out.println("code = " + code);
    }

    @GetMapping("/book/{book_id}")
    public ResponseEntity<Map<String, Map>> getBook(@PathVariable("book_id") @Valid Long id) throws JsonProcessingException {
        return ResponseEntity.ok(bookService.testListCount(id));
    }

    @GetMapping("/bookss/{book_id}")
    public ResponseEntity<List<Tuple>> getTuple(@PathVariable("book_id") @Valid Long id) {
        return ResponseEntity.ok(bookService.maybetuple(id));
    }

}

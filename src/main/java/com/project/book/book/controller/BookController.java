package com.project.book.book.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.book.book.domain.Book;
import com.project.book.book.dto.request.BookRequestDto;
import com.project.book.book.service.BookService;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // 책 저장 로직
    @PostMapping("/books")
    public ResponseEntity saveBook(@RequestBody @Valid BookRequestDto request) {
        Book book = bookService.createOrRegisterBook(request);
        URI uri = URI.create("/books/" + book.getId());

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/books/{book_id}")
    public ResponseEntity<Map<String, Object>> getDetailBook(@PathVariable("book_id") @Valid Long id) throws JsonProcessingException {
        Map<String, Object> detailBook = bookService.getDetailBook(id);

        System.out.println("detailBook = " + detailBook.toString());
        return ResponseEntity.ok(bookService.getDetailBook(id));
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

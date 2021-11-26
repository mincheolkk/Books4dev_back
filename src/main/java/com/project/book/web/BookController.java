package com.project.book.web;


import com.project.book.domain.Book;
import com.project.book.dto.book.BookRequsetDto;
import com.project.book.dto.kakao.KaKaoBookInfoDto;
import com.project.book.service.BookService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.net.URI;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // 책 저장 로직
    @PostMapping("/books")
    public ResponseEntity saveBook(@RequestBody @Valid BookRequsetDto request) {
        Book book = bookService.createOrRegisterBook(request);
        URI uri = URI.create("/books/" + book.getId());

        return ResponseEntity.created(uri).build();
    }
}

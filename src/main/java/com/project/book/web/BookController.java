package com.project.book.web;


import com.project.book.domain.Book;
import com.project.book.dto.book.BookRequestDto;
import com.project.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    public ResponseEntity<List<Map>> getDetailBook(@PathVariable("book_id") @Valid Long id) {
        List<Map> detailBook = bookService.getDetailBook(id);

        System.out.println("detailBook = " + detailBook.toString());
        return ResponseEntity.ok(bookService.getDetailBook(id));
    }
}

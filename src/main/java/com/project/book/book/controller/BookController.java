package com.project.book.book.controller;

import com.project.book.book.domain.Book;
import com.project.book.book.dto.request.*;
import com.project.book.book.dto.response.AllBookResponseDto;
import com.project.book.book.dto.response.BookResponseDto;
import com.project.book.book.service.BookService;
import com.project.book.common.config.jwt.LoginMember;
import com.project.book.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    // 책 저장 로직
    @PostMapping("/fromSearch")
    public ResponseEntity saveBookFromSearch(
                                            @LoginMember final Member member,
                                            @RequestBody @Valid final SaveBookFromSearchDto request
    ) {
        Book book = bookService.saveBookFromSearch(request, member);
        return ResponseEntity.created(
                        URI.create("/book/" + book.getId()))
                .build();
    }

    @PostMapping("/fromList")
    public ResponseEntity saveBookFromList(
                                            @LoginMember final Member member,
                                            @RequestBody @Valid final SaveBookFromListDto request
    ) {
        Book book = bookService.saveBookFromList(request, member);

        return new ResponseEntity<>(book.getId(), HttpStatus.OK);

    }

    @PostMapping("/wish")
    public ResponseEntity<?> saveWishBook(
                                            @LoginMember final Member member,
                                            @RequestBody @Valid final BookDataDto request
    ) {
        if (!request.validCheck()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return bookService.saveWishBook(request, member);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllBooks(
                                        @ModelAttribute final AllBookFilterDto condition,
                                        @ModelAttribute CustomPageRequest customPageRequest) {
        ResponseEntity<?> result = bookService.getAllBook(condition, customPageRequest.toPageable());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/readbook")
    public List<AllBookResponseDto> findBookBySearch(@RequestParam final String query) {
        return bookService.findBookBySearch(query);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetail(@PathVariable final Long id) {
        BookResponseDto bookResponseDto = bookService.getDetailBook(id);
        return new ResponseEntity<>(bookResponseDto, HttpStatus.ACCEPTED);
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getPopularKeyword() {
        return bookService.getPopularKeyword();
    }

}


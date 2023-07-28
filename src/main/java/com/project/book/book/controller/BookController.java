package com.project.book.book.controller;

import com.project.book.book.dto.request.*;
import com.project.book.book.dto.response.BookResponseDto;
import com.project.book.book.dto.response.KeywordScoreResponseDto;
import com.project.book.book.service.BookService;
import com.project.book.book.service.RankingService;
import com.project.book.book.service.WishBookService;
import com.project.book.common.config.jwt.LoginMember;
import com.project.book.member.dto.LoginMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final WishBookService wishBookService;
    private final RankingService rankingService;

    // 검색을 통한 kakao 데이터를 통한 <읽은 책> 저장
    @PostMapping("/fromSearch")
    public ResponseEntity<Void> saveBookFromKakao(
                                            @LoginMember final LoginMemberDto loginMemberDto,
                                            @RequestBody @Valid final SaveBookFromSearchDto request
    ) {
        Long bookId = bookService.saveBookFromKakao(loginMemberDto.getOAuth(), request);
        return ResponseEntity.created(
                        URI.create("/book/" + bookId))
                .build();
    }

    // 내 서비스에 저장된 책 데이터를 통한 <읽은 책> 저장
    @PostMapping("/fromList")
    public ResponseEntity saveBookFromBooks4dev(
                                            @LoginMember final LoginMemberDto loginMemberDto,
                                            @RequestBody @Valid final SaveBookFromListDto request
    ) {
        bookService.saveBookFromBooks4dev(loginMemberDto.getOAuth(), request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/wish")
    public ResponseEntity<Void> saveWishBook(
                                            @LoginMember final LoginMemberDto loginMemberDto,
                                            @RequestBody @Valid final BookInfoDto request
    ) {
        wishBookService.saveWishBook(loginMemberDto.getOAuth(), request);
        return ResponseEntity.ok().build();
    }

    // 전체 책 조회
    @GetMapping("/all")
    public ResponseEntity<Page<?>> getAllBooks(
                                        @ModelAttribute final AllBookFilterDto condition,
                                        @ModelAttribute CustomPageRequest customPageRequest) {
        Page<?> allBook = bookService.getAllBook(condition, customPageRequest.toPageable());
        return ResponseEntity.ok(allBook);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getDetail(@PathVariable final Long id) {
        BookResponseDto bookResponseDto = bookService.getDetailBook(id);
        return ResponseEntity.ok(bookResponseDto);
    }

    // 인기 검색어 조회
    @GetMapping("/popular")
    public ResponseEntity<List<KeywordScoreResponseDto>> getPopularKeyword() {
        List<KeywordScoreResponseDto> popularKeyword = rankingService.getPopularKeyword();
        return ResponseEntity.ok(popularKeyword);
    }

}


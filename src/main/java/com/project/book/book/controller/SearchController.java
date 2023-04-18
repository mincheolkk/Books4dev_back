package com.project.book.book.controller;

import com.project.book.book.dto.kakao.KakaoBookDto;
import com.project.book.book.service.BookService;
import com.project.book.common.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@RestController
@RequiredArgsConstructor
public class SearchController {

    private final WebClient kakaoWebClient;
    private final RedisUtil redisUtil;
    private final BookService bookService;

    // 책 검색 - 카카오 책검색 api
    @GetMapping("/kakao/search")
    public Mono<KakaoBookDto> search(@RequestParam final String query) {
        Mono<KakaoBookDto> kakaoBookDtoMono = kakaoWebClient.get()
                .uri(builder -> builder.path("/v3/search/book")
                        .queryParam("page", "1")
                        .queryParam("size", "30")
                        .queryParam("target", "title")
                        .queryParam("query", query)
                        .build())
                .exchangeToMono(response -> response.bodyToMono(KakaoBookDto.class))
                        .doOnSuccess(kakaoBookDto -> {
                            int bookCount = kakaoBookDto.getDocuments().size();
                            if (bookCount > 0) {
                                redisUtil.getSearchKeywords(query);
                        }
            });

        return kakaoBookDtoMono;
    }

    // 책 검색 - books4dev 데이터 검색
    @GetMapping("/book/search/readbook")
    public ResponseEntity<?> findBookBySearch(@RequestParam final String query) {
        if (query.length() < 2) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(bookService.findBookBySearch(query), HttpStatus.OK);
    }
}
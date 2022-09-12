package com.project.book.member.controller;

import com.project.book.book.dto.kakao.KakaoBookDto;
import com.project.book.book.dto.response.AllBookResponseDto;
import com.project.book.book.dto.response.SearchBookResponseDto;
import com.project.book.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
//@RequestMapping("/kakao/*")
@RequiredArgsConstructor
public class KakaoController {

    private final WebClient kakaoWebClient;
    private final BookService bookService;

    @GetMapping("/todo")
//    public Mono<KakaoBookDto> search(@RequestParam String query, @LoginMember Member member) {
    public SearchBookResponseDto search(@RequestParam String query) {
        System.out.println("query = " + query);

        List<AllBookResponseDto> registeredBook = bookService.findRegisteredBook(query);


        Mono<KakaoBookDto> kakaoBookDtoMono = kakaoWebClient.get()
                .uri(builder -> builder.path("/v3/search/book")
                        .queryParam("page", "1")
                        .queryParam("size", "20")
                        .queryParam("query", query)
                        .build())
                .exchangeToMono(response -> {
                    return response.bodyToMono(KakaoBookDto.class);
                });
//        return kakaoWebClient.get()
//                .uri(builder -> builder.path("/v3/search/book")
//                        .queryParam("page", "1")
//                        .queryParam("size", "20")
//                        .queryParam("query", query)
//                        .build())
//                .exchangeToMono(response -> {
//                    return response.bodyToMono(KakaoBookDto.class);
//                });

        SearchBookResponseDto searchBookResponseDto = new SearchBookResponseDto(registeredBook, kakaoBookDtoMono);
        System.out.println("searchBookResponseDto = " + searchBookResponseDto);
        System.out.println("searchBookResponseDto.getAllBookResponseDto().get(0) = " + searchBookResponseDto.getAllBookResponseDto().get(0));

        return searchBookResponseDto;
    }
}
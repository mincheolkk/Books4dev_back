package com.project.book.member.controller;

import com.project.book.book.dto.kakao.KakaoBookDto;
import com.project.book.common.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/kakao")
@RequiredArgsConstructor
public class KakaoController {

    private final WebClient kakaoWebClient;
    private final RedisUtil redisUtil;

    @GetMapping("/search")
    public Mono<KakaoBookDto> search(@RequestParam final String query) {
        System.out.println("query = " + query);

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
                                redisUtil.incrementRankingScore(query);
                        }
            });

        return kakaoBookDtoMono;
    }



}
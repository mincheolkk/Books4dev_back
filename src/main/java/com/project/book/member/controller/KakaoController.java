package com.project.book.member.controller;

import com.project.book.book.dto.kakao.KakaoBookDto;
import com.project.book.common.config.jwt.LoginMember;
import com.project.book.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
//@RequestMapping("/kakao/*")
@RequiredArgsConstructor
public class KakaoController {

    private final WebClient kakaoWebClient;

    @GetMapping("/todo")
    public Mono<KakaoBookDto> search(@RequestParam String query, @LoginMember Member member) {
        return kakaoWebClient.get()
                .uri(builder -> builder.path("/v3/search/book")
                        .queryParam("page", "1")
                        .queryParam("size", "10")
                        .queryParam("query", query)
                        .build())
                .exchangeToMono(response -> {
                    return response.bodyToMono(KakaoBookDto.class);
                });
    }
}
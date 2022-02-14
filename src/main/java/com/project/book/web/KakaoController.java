package com.project.book.web;

import com.project.book.dto.kakao.KakaoBookDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/kakao/*")
@RequiredArgsConstructor
public class KakaoController {

    private final WebClient kakaoWebClient;

    @GetMapping("/search")
    public Mono<KakaoBookDto> search(@RequestParam String query) {
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
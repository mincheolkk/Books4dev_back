package com.project.book.web;

import com.project.book.configuration.properties.KakaoProperties;
import com.project.book.dto.KakaoBookResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/kakao/*")
@RequiredArgsConstructor
public class KakaoController {

    private final WebClient kakaoWebClient;

    @GetMapping("/search")
    public Mono<KakaoBookResponseDto> search(@RequestParam String query) {
        return kakaoWebClient.get()
                .uri(builder -> builder.path("/v3/search/book")
                        .queryParam("query", query)
                        .build())
                .exchangeToMono(response -> {
                    return response.bodyToMono(KakaoBookResponseDto.class);
                });
    }
}

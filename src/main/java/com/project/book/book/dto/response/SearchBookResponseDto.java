package com.project.book.book.dto.response;

import com.project.book.book.dto.kakao.KakaoBookDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@Getter
@NoArgsConstructor
public class SearchBookResponseDto {

    public List<AllBookResponseDto> allBookResponseDto;
    public Mono<KakaoBookDto> kakaoBookDtoMono;

    public SearchBookResponseDto(List<AllBookResponseDto> allBookResponseDto, Mono<KakaoBookDto> kakaoBookDtoMono) {
        this.allBookResponseDto = allBookResponseDto;
        this.kakaoBookDtoMono = kakaoBookDtoMono;
    }
}

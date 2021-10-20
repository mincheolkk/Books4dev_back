package com.project.book.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class KakaoBookResponseDto {

    private List<BookInfoDto> documents;

}

package com.project.book.book.dto.kakao;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KaKaoBookInfoDto {

    private String title;
    private List<String> authors;
    private List<String> translators;
    private String isbn;
    private ZonedDateTime datetime;
    private String publisher;
    private Long price;
    private String thumbnail;

}

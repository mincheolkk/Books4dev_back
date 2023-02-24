package com.project.book.book.dto.response;


import com.project.book.book.domain.WishMember;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
public class WishBookResponseDto {

    private Long id;
    private String title;
    private String isbn;
    private String thumbnail;

    @QueryProjection
    public WishBookResponseDto(Long id, String title, String isbn, String thumbnail) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.thumbnail = thumbnail;
    }
}

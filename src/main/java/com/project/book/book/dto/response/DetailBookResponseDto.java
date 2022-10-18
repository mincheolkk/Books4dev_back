package com.project.book.book.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class DetailBookResponseDto {

    private String title;
    private String authors;
    private String translator;
    private String publisher;
    private String thumbnail;
    private String isbn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime releaseDate;
    private Long price;

    private Double avgStar;

    @QueryProjection
    public DetailBookResponseDto(String title, String authors, String translator, String publisher, String thumbnail, String isbn, LocalDateTime releaseDate, Long price, Double avgStar) {
        this.title = title;
        this.authors = authors;
        this.translator = translator;
        this.publisher = publisher;
        this.thumbnail = thumbnail;
        this.isbn = isbn;
        this.releaseDate = releaseDate;
        this.price = price;
        this.avgStar = avgStar;
    }

    public void updateAvgStar() {
        this.avgStar = ((double) Math.round((this.avgStar * 100 )/ 10))/10;
    }
}

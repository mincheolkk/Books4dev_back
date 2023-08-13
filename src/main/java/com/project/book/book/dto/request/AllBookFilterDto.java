package com.project.book.book.dto.request;

import com.project.book.book.domain.BookSortType;
import com.project.book.book.domain.BookTime;
import com.project.book.member.domain.MemberType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
public class AllBookFilterDto {

    private MemberType memberType;

    private BookSortType sortType;

    private BookTime recommendType;

    private Long totalCount;

}

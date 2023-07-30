package com.project.book.book.repository;

import com.project.book.book.dto.request.AllBookFilterDto;
import com.project.book.book.dto.response.BookResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookRepositoryCustom {

    List<BookResponseDto> getAllBooks(final AllBookFilterDto condition, final Pageable pageable);

    List<BookResponseDto> findBookBySearch(final String text);

    Long countAllBooks(final AllBookFilterDto condition);

}

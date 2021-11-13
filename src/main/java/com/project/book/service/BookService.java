package com.project.book.service;

import com.project.book.domain.Book;
import com.project.book.dto.kakao.KaKaoBookInfoDto;
import com.project.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public void createBook(@RequestBody @Valid KaKaoBookInfoDto request) {

        String isbn = request.getIsbn();
        Long bookIsbn = bookRepository.findByIsbn(isbn);

        if (bookIsbn == -1) {
            bookRepository.save(request.createbook())
        }
        request.registerbook()
    }

    public void checkIsbn(String isbn) {

    }

    // isbn 으로 등록되어 있는지 확인
    // 등록되어 있으면 책 아이디, 없으면 등록 후 책 아이디
    // 그리고 registerBook 채우기

    private String combineAuthors(List<String> authors) {
        if (Objects.isNull(authors) || authors.isEmpty()) {
            return Strings.EMPTY;
        }
        return String.join(",", authors);
    }

    private String combineTranslators(List<String> translators) {
        if (Objects.isNull(translators) || translators.isEmpty()) {
            return Strings.EMPTY;
        }
        return String.join(",", translators);
    }
}

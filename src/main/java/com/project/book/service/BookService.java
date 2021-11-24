package com.project.book.service;

import com.project.book.domain.Book;
import com.project.book.domain.RegisterBook;
import com.project.book.dto.book.BookRequsetDto;
import com.project.book.dto.book.CreateBookRequestDto;
import com.project.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public void createBook(@RequestBody @Valid BookRequsetDto request) {

        String isbn = request.getIsbn();
        Book bookIsbn = bookRepository.findByIsbn(isbn);

        if (bookIsbn == null) {
            CreateBookRequestDto createbook = CreateBookRequestDto.builder()
                    .authors(listToString(request.getAuthors()))
                    .translator(listToString(request.getTranslator()))
                    .title(request.getTitle())
                    .publisher(request.getPublisher())
                    .price(request.getPrice())
                    .thumbnail(request.getThumbnail())
                    .datetime(request.getDatetime().toLocalDateTime())
                    .isbn(isbn)
                    .build();

            Book book = bookRepository.save(createbook.toEntity());
            Long bookId = book.getId();

            RegisterBook.builder()
                    .book(book)
                    .readTime(request.getReadTime())
                    .recommendTime(request.getRecommendTime())
                    .star(request.getStar())
                    .build();
        }
        else if (bookIsbn != null) {
            RegisterBook.builder()
                    .book(bookIsbn)
                    .readTime(request.getReadTime())
                    .recommendTime(request.getRecommendTime())
                    .star(request.getStar())
                    .build();
        }
    }

    private static String listToString(List<String> list) {
        if (list.isEmpty()) {
            return Strings.EMPTY;
        }
        return String.join(",", list);
    }
}

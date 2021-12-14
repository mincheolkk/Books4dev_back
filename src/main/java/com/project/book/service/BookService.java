package com.project.book.service;

import com.project.book.domain.Book;
import com.project.book.domain.RegisterBook;
import com.project.book.dto.book.BookRequestDto;
import com.project.book.dto.book.CreateBookRequestDto;
import com.project.book.repository.BookRepository;
import com.project.book.repository.RegisterBookRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final RegisterBookRepository registerBookRepository;

    public Book createOrRegisterBook(@RequestBody @Valid BookRequestDto request) {

        String isbn = request.getIsbn();
        Book savedBook = bookRepository.findByIsbn(isbn);

        if (savedBook == null) {
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

            Book newBook = bookRepository.save(createbook.toEntity());

            RegisterBook registerBook = requestRegisterBook(newBook, request);
            registerBookRepository.save(registerBook);

            return newBook;
        }
        else if (savedBook != null) {
            RegisterBook registerBook = requestRegisterBook(savedBook, request);
            registerBookRepository.save(registerBook);
        }
        return savedBook;
    }

    private static RegisterBook requestRegisterBook(Book book, BookRequestDto request) {
        return RegisterBook.builder()
                .book(book)
                .readTime(request.getReadTime())
                .recommendTime(request.getRecommendTime())
                .star(request.getStar())
                .build();
    }

    private static String listToString(List<String> list) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return Strings.EMPTY;
        }
        return String.join(",", list);
    }

    public List<Map> getDetailBook(Long id) {
        List<Map> detailBook = bookRepository.getDetailBook(id);

        return detailBook;
    }
}
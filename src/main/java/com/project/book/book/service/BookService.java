package com.project.book.book.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.book.book.domain.Book;
import com.project.book.book.domain.RegisterBook;
import com.project.book.book.dto.request.BookRequestDto;
import com.project.book.book.dto.request.CreateBookRequestDto;
import com.project.book.book.repository.BookRepository;
import com.project.book.book.repository.RegisterBookRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final RegisterBookRepository registerBookRepository;

    @Transactional
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
                .readBookTime(request.getReadTime())
                .recommendBookTime(request.getRecommendTime())
                .star(request.getStar())
                .build();
    }

    private static String listToString(List<String> list) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return Strings.EMPTY;
        }
        return String.join(",", list);
    }

    public Map<String, Object> getDetailBook(Long id) throws JsonProcessingException {
        Optional<Book> book = bookRepository.findById(id);
        Map<String, Object> detailBook = bookRepository.getDetailBook(book.get());

        return detailBook;
    }

    public Map<String, Map> testListCount(Long id) throws JsonProcessingException {
        Optional<Book> book = bookRepository.findById(id);

        return bookRepository.testListCount(book.get());
    }

    public List<Tuple> maybetuple(Long id) {
        Optional<Book> book = bookRepository.findById(id);

        return bookRepository.maybetuple(book.get());
    }
}
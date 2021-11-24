package com.project.book.service;

import com.project.book.domain.Book;
import com.project.book.domain.RegisterBook;
import com.project.book.dto.book.BookRequsetDto;
import com.project.book.dto.book.CreateBookRequestDto;
import com.project.book.repository.BookRepository;
import com.project.book.repository.RegisterBookRepository;
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
    private final RegisterBookRepository registerBookRepository;

    public Book createOrRegisterBook(@RequestBody @Valid BookRequsetDto request) {

        String isbn = request.getIsbn();
        Book bookIsbn = bookRepository.findByIsbn(isbn);

        System.out.println("bookIsbn = " + bookIsbn);


        if (bookIsbn == null) {
            System.out.println("in if");
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

            System.out.println("createbook = " + createbook);

            Book book = bookRepository.save(createbook.toEntity());
            System.out.println("book = " + book);

            RegisterBook building = RegisterBook.builder()
                    .book(book)
                    .readTime(request.getReadTime())
                    .recommendTime(request.getRecommendTime())
                    .star(request.getStar())
                    .build();

            RegisterBook registerBook = registerBookRepository.save(building);

            System.out.println("registerBook.getBook() = " + registerBook.getBook());
            System.out.println("registerBook.getId() = " + registerBook.getId());

            return book;
        }
        else if (bookIsbn != null) {
            RegisterBook building = RegisterBook.builder()
                    .book(bookIsbn)
                    .readTime(request.getReadTime())
                    .recommendTime(request.getRecommendTime())
                    .star(request.getStar())
                    .build();

            RegisterBook registerBook = registerBookRepository.save(building);
        }
        return bookIsbn;
    }

    private static String listToString(List<String> list) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return Strings.EMPTY;
        }
        return String.join(",", list);
    }
}
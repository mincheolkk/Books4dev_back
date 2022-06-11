package com.project.book.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.book.book.domain.Book;
import com.project.book.book.domain.BookTime;
import com.project.book.book.dto.request.BookRequestDto;
import com.project.book.book.repository.BookRepository;
import com.project.book.book.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@SpringBootTest
public class BookServiceTest {

    @Autowired
    BookService bookService;
    @Autowired BookRepository repository;

    @Test
    @Commit
    public void 책등록() throws Exception {

        //given

        List<String> testAuthors = new ArrayList<>();
        testAuthors.add("test");
        testAuthors.add("test2");
        System.out.println("testAuthors = " + testAuthors);


        BookRequestDto requsetDto = BookRequestDto.builder()
                .datetime(ZonedDateTime.now())
                .authors(testAuthors)
                .translator(null)
                .isbn("123131313")
                .title("test")
                .price(0L)
                .thumbnail("url:test")
                .publisher("tes")
                .readTime(BookTime.fiveYear)
                .recommendTime(BookTime.before)
                .star(1)
                .build();

        BookRequestDto requsetDto2 = BookRequestDto.builder()
                .datetime(ZonedDateTime.now())
                .authors(testAuthors)
                .translator(null)
                .isbn("999999")
                .title("test2")
                .price(0L)
                .thumbnail("url:test")
                .publisher("tes")
                .readTime(BookTime.before)
                .recommendTime(BookTime.after)
                .star(5)
                .build();

        Book orRegisterBook = bookService.createOrRegisterBook(requsetDto);
        bookService.createOrRegisterBook(requsetDto2);

        System.out.println("orRegisterBook = " + orRegisterBook);
//        System.out.println("orRegisterBook.getIsbn() = " + orRegisterBook.getIsbn());
        System.out.println("orRegisterBook.getAuthors() = " + orRegisterBook.getAuthors());
        System.out.println("orRegisterBook.getTranslator() = " + orRegisterBook.getTranslator());
        //when

        //then
    }

    @Test
    void 책디테일뷰() throws JsonProcessingException {

        Optional<Book> byId = repository.findById(1L);
        System.out.println("byId.get() = " + byId.get());


        repository.getDetailBook(byId.get());
//        repository.testListCount(byId.get());
    }

    @Test
    void 읽은시간리스트뽑기() throws JsonProcessingException {
        Optional<Book> byId = repository.findById(1L);
        System.out.println("++++++++++++");

        repository.testListCount(byId.get());
    }
}

package com.project.book.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.book.book.domain.Book;
import com.project.book.book.domain.BookTime;
import com.project.book.book.domain.RegisterBook;
import com.project.book.book.dto.request.*;
import com.project.book.book.repository.BookRepository;
import com.project.book.book.repository.RegisterBookRepository;
import com.project.book.book.service.BookService;
import com.project.book.member.domain.Member;
import com.project.book.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.project.book.book.domain.BookTime.*;
import static com.project.book.member.domain.MemberType.*;
import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
public class BookServiceTest {

    @Autowired
    BookService bookService;
    @Autowired BookRepository bookRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RegisterBookRepository registerBookRepository;

    private Member member;

    private Book book;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .oAuth("01")
                .type(BackEnd)
                .build();

        book = Book.builder()
                .title("test_book")
                .isbn("1111")
                .price(12345L)
                .publisher("test_publisher")
                .authors("test_authors")
                .build();

        memberRepository.save(member);
        bookRepository.save(book);
    }

    @AfterEach
    void tearDown() {
        memberRepository.delete(member);
        bookRepository.delete(book);
    }

    @DisplayName("검색해서 책 등록")
    @Test
    void registerBySearch() {
        // given
        BookDataDto bookDataDto = BookDataDto.builder()
                .title("book")
                .datetime(ZonedDateTime.now())
                .isbn("12345")
                .price(12345L)
                .build();

        BookReviewDto bookReviewDto = BookReviewDto.builder()
                .readTime(after)
                .recommendTime(twoYear)
                .star(5)
                .build();

        RegisterBySearchDto registerBySearchDto = RegisterBySearchDto.builder()
                .item(bookDataDto)
                .review(bookReviewDto)
                .build();

        // when
        Book bookBySearch = bookService.registerBySearch(registerBySearchDto, member);
        RegisterBook registerBook = registerBookRepository.findByMemberAndBookAndReadTime(member, bookBySearch, after);

        // then
        assertThat(bookBySearch.getIsbn()).isEqualTo("12345");
        assertThat(bookBySearch.getPrice()).isEqualTo(12345L);

        assertThat(registerBook.getBook()).isEqualTo(bookBySearch);
        assertThat(registerBook.getMember()).isEqualTo(member);
        assertThat(member.getRegisterBooks().size()).isEqualTo(0);
    }
    @DisplayName("홈 화면에서 책 등록")
    @Test
    void registerByHome() {
        BookReviewDto bookReviewDto = BookReviewDto.builder()
                .readTime(after)
                .recommendTime(twoYear)
                .star(2)
                .build();

        RegisterByHomeListDto registerByHomeListDto = RegisterByHomeListDto.builder()
                .isbn("1111")
                .review(bookReviewDto)
                .build();

        Book book = bookService.registerByHomeList(registerByHomeListDto, member);

        assertThat(book.getPrice()).isEqualTo(12345L);
        assertThat(book.getIsbn()).isEqualTo("1111");
        assertThat(book.getPublisher()).isEqualTo("test_publisher");
        assertThat(book.getRecommendTime().getTwoYearCount()).isEqualTo(1);
    }

    @DisplayName("관심 등록")
    @Test
    void wishRegister() {
        WishBookRequestDto wishBookRequestDto = WishBookRequestDto.create("1111", "test_book", "test::thumbanil");
        bookService.saveWishBook(wishBookRequestDto, member);

        assertThat(book.getStarAndCount().getWishCount()).isEqualTo(1);
    }
}

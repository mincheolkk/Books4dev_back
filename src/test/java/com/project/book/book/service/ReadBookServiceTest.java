package com.project.book.book.service;

import com.project.book.book.domain.Book;
import com.project.book.book.domain.BookInfo;
import com.project.book.book.domain.BookTime;
import com.project.book.book.domain.ReadBook;
import com.project.book.book.dto.request.BookReviewDto;
import com.project.book.book.dto.response.ReadBookResponseDto;
import com.project.book.book.repository.BookRepository;
import com.project.book.book.repository.ReadBookRepository;
import com.project.book.member.domain.Member;
import com.project.book.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.project.book.book.domain.BookTime.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ReadBookServiceTest {

    @Autowired
    private ReadBookRepository readBookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReadBookService readBookService;

    @DisplayName("<읽은 책>을 저장하면, 해당 책의 ReadCount가 1 올라간다.")
    @Test
    void saveReadBook() {
        // given
        Member member = Member.builder()
                .build();
        memberRepository.save(member);

        Book book = Book.builder()
                .build();
        bookRepository.save(book);

        BookReviewDto bookReviewDto = BookReviewDto.builder()
                .searchKeyword("MySQL")
                .readTime(threeYear)
                .recommendTime(anyTime)
                .star(5).build();

        // when
        readBookService.saveReadBook(member, book, bookReviewDto);

        // then
        assertThat(book.getCount().getReadCount()).isEqualTo(1);
    }

    @DisplayName("유저의 리뷰 데이터를 기반으로 <읽은 책>을 저장한다.")
    @Test
    void saveReadBook2() {
        // given
        Member member = Member.builder()
                .build();
        memberRepository.save(member);

        Book book = Book.builder()
                .build();
        bookRepository.save(book);

        BookTime readBookTime = threeYear;
        BookTime recommendTime = anyTime;
        Integer star = 5;
        BookReviewDto bookReviewDto = BookReviewDto.builder()
                .readTime(readBookTime)
                .recommendTime(recommendTime)
                .star(star).build();

        // when
        readBookService.saveReadBook(member, book, bookReviewDto);

        // then
        ReadBook readBook = readBookRepository.findByMemberAndBookAndReadTime(member, book, readBookTime);
        assertAll(
                () -> {
                    assertThat(readBook.getBook()).isSameAs(book);
                    assertThat(readBook.getReadBookTime()).isEqualTo(readBookTime);
                    assertThat(readBook.getRecommendBookTime()).isEqualTo(recommendTime);
                    assertThat(readBook.getStar()).isEqualTo((double) star);
                }
        );
    }

    @DisplayName("<읽은 책>은 <읽은 시기>에 따라서 여러 번 저장이 가능하다. ")
    @Test
    void saveReadBook3() {
        // given
        Member member = Member.builder()
                .build();
        memberRepository.save(member);

        Book book = Book.builder()
                .build();
        bookRepository.save(book);

        BookTime readBookTime = threeYear;
        BookTime recommendTime = anyTime;
        Integer star = 5;

        ReadBook readBook = ReadBook.builder()
                .book(book)
                .member(member)
                .readBookTime(readBookTime)
                .recommendBookTime(recommendTime)
                .star(star)
                .build();
        readBookRepository.save(readBook);

        // when
        BookTime newReadBookTime = before;
        BookTime newRecommendTime = before;
        Integer newStar = 1;

        BookReviewDto bookReviewDto = BookReviewDto.builder()
                .readTime(newReadBookTime)
                .recommendTime(newRecommendTime)
                .star(newStar).build();

        readBookService.saveReadBook(member, book, bookReviewDto);

        // then
        ReadBook readBook1 = readBookRepository.findByMemberAndBookAndReadTime(member, book, readBookTime);
        ReadBook readBook2 = readBookRepository.findByMemberAndBookAndReadTime(member, book, newReadBookTime);
        assertAll(
                () -> {
                    assertThat(readBook1.getBook()).isEqualTo(readBook2.getBook());
                    assertThat(readBook1.getMember()).isEqualTo(readBook2.getMember());
                    assertThat(readBook1.getReadBookTime()).isNotEqualTo(readBook2.getReadBookTime());
                    assertThat(readBook1.getRecommendBookTime()).isNotEqualTo(readBook2.getRecommendBookTime());
                    assertThat(readBook1.getStar()).isNotEqualTo(readBook2.getStar());
                }
        );
    }

    @DisplayName("이미 저장된 <읽은 시기>에 저장할 경우, 리뷰 데이터는 업데이트된다.")
    @Test
    void saveReadBook4() {
        // given
        Member member = Member.builder()
                .build();
        memberRepository.save(member);

        Book book = Book.builder()
                .build();
        bookRepository.save(book);

        BookTime readBookTime = threeYear;
        BookTime recommendTime = anyTime;
        Integer star = 5;

        ReadBook readBook = ReadBook.builder()
                .book(book)
                .member(member)
                .readBookTime(readBookTime)
                .recommendBookTime(recommendTime)
                .star(star)
                .build();
        readBookRepository.save(readBook);

        // when
        BookTime newRecommendTime = before;
        Integer newStar = 1;

        BookReviewDto bookReviewDto = BookReviewDto.builder()
                .readTime(readBookTime)
                .recommendTime(newRecommendTime)
                .star(newStar).build();

        readBookService.saveReadBook(member, book, bookReviewDto);

        // then
        ReadBook updatedReadBook = readBookRepository.findByMemberAndBookAndReadTime(member, book, threeYear);
        assertAll(
                () -> {
                    assertThat(updatedReadBook.getBook()).isSameAs(book);
                    assertThat(updatedReadBook.getReadBookTime()).isEqualTo(readBookTime);
                    assertThat(updatedReadBook.getRecommendBookTime()).isEqualTo(newRecommendTime);
                    assertThat(updatedReadBook.getStar()).isEqualTo((double) newStar);
                }
        );
    }

    @DisplayName("책의 평점은 <읽은 책>에 저장된 평점 데이터의 평균으로 구한다.")
    @Test
    void calculateAvgStar() {
        // given
        Member member = Member.builder()
                .build();
        memberRepository.save(member);

        Member member2 = Member.builder()
                .build();
        memberRepository.save(member2);

        Book book = Book.builder()
                .build();
        bookRepository.save(book);

        Integer star = 5;
        ReadBook readBook = ReadBook.builder()
                .book(book)
                .member(member)
                .readBookTime(threeYear)
                .recommendBookTime(anyTime)
                .star(star)
                .build();
        readBookRepository.save(readBook);

        Integer newStar = 1;
        ReadBook readBook2 = ReadBook.builder()
                .member(member2)
                .book(book)
                .readBookTime(before)
                .recommendBookTime(before)
                .star(newStar)
                .build();
        readBookRepository.save(readBook2);

        // when
        readBookService.calculateAvgStar(book);

        // then
        assertThat(book.getStar().getAvgStar()).isEqualTo((double) 3);
    }

    @DisplayName("유저가 저장한 <읽은 책>을 <읽은 시기>를 키로 가져온다.")
    @Test
    void getMemberReadBook() {
        // given
        Long oAuth = 123L;
        Member member = Member.builder()
                .oAuth(String.valueOf(oAuth))
                .build();
        memberRepository.save(member);

        BookInfo bookInfo1 = BookInfo.builder()
                .isbn("1")
                .title("ProGit")
                .build();
        Book book = Book.builder()
                .bookInfo(bookInfo1)
                .build();
        bookRepository.save(book);

        ReadBook readBook = ReadBook.builder()
                .book(book)
                .member(member)
                .readBookTime(before)
                .recommendBookTime(threeYear)
                .star(5)
                .build();
        readBookRepository.save(readBook);

        BookInfo bookInfo2 = BookInfo.builder()
                .isbn("2")
                .title("MySQL8.0")
                .build();
        Book book2 = Book.builder()
                .bookInfo(bookInfo2)
                .build();
        bookRepository.save(book2);

        ReadBook readBook2 = ReadBook.builder()
                .book(book2)
                .member(member)
                .readBookTime(after)
                .recommendBookTime(anyTime)
                .star(3)
                .build();
        readBookRepository.save(readBook2);

        // when
        Map<BookTime, List<ReadBookResponseDto>> result = readBookService.getMemberReadBook(oAuth);

        // then
        assertAll(
                () -> {
                    assertThat(result.get(before)).hasSize(1)
                            .extracting("isbn", "title")
                            .containsExactlyInAnyOrder(
                                    tuple("1", "ProGit")
                            );
                    assertThat(result.get(after)).hasSize(1).extracting("isbn", "title")
                            .containsExactlyInAnyOrder(
                                    tuple("2", "MySQL8.0")
                            );
                    assertThat(result.get(threeYear)).isNull();
                    assertThat(result.get(sixYear)).isNull();
                }
        );
    }
}
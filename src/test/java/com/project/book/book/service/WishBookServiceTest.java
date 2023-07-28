package com.project.book.book.service;

import com.project.book.book.domain.Book;
import com.project.book.book.domain.BookInfo;
import com.project.book.book.domain.WishBook;
import com.project.book.book.dto.request.BookInfoDto;
import com.project.book.book.dto.response.WishBookResponseDto;
import com.project.book.book.repository.BookRepository;
import com.project.book.book.repository.WishBookRepository;
import com.project.book.common.exception.AlreadyExistException;
import com.project.book.member.domain.Member;
import com.project.book.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

@SpringBootTest
class WishBookServiceTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WishBookRepository wishBookRepository;

    @Autowired
    private WishBookService wishBookService;

    @AfterEach
    void tearDown() {
        wishBookRepository.deleteAllInBatch();
        bookRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("<관심있는 책>을 저장하면, 해당 책의 wishCount가 1 올라간다.")
    @Test
    void saveWishBook() {
        // given
        Member member = Member.builder()
                .oAuth("123")
                .build();
        memberRepository.save(member);

        String title = "CleanCode";
        String isbn = "123456789";
        BookInfo bookInfo = BookInfo.builder()
                .title(title)
                .isbn(isbn)
                .build();
        Book book = Book.builder()
                .bookInfo(bookInfo)
                .build();
        bookRepository.save(book);

        // when
        BookInfoDto request = BookInfoDto.builder()
                .title(title)
                .isbn(isbn)
                .build();
        wishBookService.saveWishBook(member.getOAuth(), request);

        // then
        Book savedBook = bookRepository.findByIsbn(isbn);
        boolean existWishBook = wishBookRepository.existByBookAndMember(savedBook, member);

        assertThat(existWishBook).isTrue();
        assertThat(savedBook.getCount().getWishCount()).isEqualTo(1);
    }

    @DisplayName("<관심있는 책>을 저장한다. 해당 책이 DB에 저장되어 있지 않으면 책도 함께 저장한다.")
    @Test
    void saveWishBook2() {
        // given
        Member member = Member.builder()
                .oAuth("123")
                .build();
        memberRepository.save(member);

        // when
        String title = "CleanCode";
        String isbn = "123456789";
        BookInfoDto request = BookInfoDto.builder()
                .title(title)
                .isbn(isbn)
                .build();

        wishBookService.saveWishBook(member.getOAuth(), request);

        // then
        Book savedBook = bookRepository.findByIsbn(isbn);
        boolean existWishBook = wishBookRepository.existByBookAndMember(savedBook, member);

        assertThat(existWishBook).isTrue();
        assertThat(savedBook.getBookInfo().getTitle()).isEqualTo(title);
        assertThat(savedBook.getBookInfo().getIsbn()).isEqualTo(isbn);
    }

    @DisplayName("이미 <관심있는 책>으로 저장된 책일 경우에는 예외가 발생한다.")
    @Test
    void alreadyExistException() {
        // given
        Member member = Member.builder()
                .oAuth("123")
                .build();
        memberRepository.save(member);

        String title = "CleanCode";
        String isbn = "123456789";
        BookInfo bookInfo = BookInfo.builder()
                .title(title)
                .isbn(isbn)
                .build();
        Book book = Book.builder()
                .bookInfo(bookInfo)
                .build();
        bookRepository.save(book);

        WishBook wishBook = new WishBook(member, book);
        wishBookRepository.save(wishBook);

        // when & then
        BookInfoDto request = BookInfoDto.builder()
                .title(title)
                .isbn(isbn)
                .build();

        assertThatThrownBy(() -> wishBookService.saveWishBook(member.getOAuth(), request))
                .isInstanceOf(AlreadyExistException.class)
                .hasMessage("이미 존재합니다.");
    }

    @DisplayName("유저가 저장한 <관심있는 책>을 리스트로 가져온다.")
    @Test
    void getMemberWishBook() {
        // given
        Long oAuth = 123L;
        Member member = Member.builder()
                .oAuth(String.valueOf(oAuth))
                .build();
        memberRepository.save(member);

        String isbn = "1";
        String title = "ProGit";

        BookInfo bookInfo = BookInfo.builder()
                .isbn(isbn)
                .title(title)
                .build();
        Book book = Book.builder()
                .bookInfo(bookInfo)
                .build();
        bookRepository.save(book);

        WishBook wishBook = new WishBook(member, book);
        wishBookRepository.save(wishBook);

        // when
        List<WishBookResponseDto> results = wishBookService.getMemberWishBook(oAuth);

        // then
        assertThat(results).hasSize(1)
                .extracting("isbn", "title")
                .containsExactlyInAnyOrder(
                        tuple(isbn,title)
                );
    }
}
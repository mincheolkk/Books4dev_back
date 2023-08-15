package com.project.book.book.service;

import com.project.book.book.domain.Book;
import com.project.book.book.domain.BookInfo;
import com.project.book.book.domain.BookTime;
import com.project.book.book.dto.request.*;
import com.project.book.book.dto.response.BookResponseDto;
import com.project.book.book.dto.response.DetailBookResponseDto;
import com.project.book.book.dto.response.KeywordScoreResponseDto;
import com.project.book.book.repository.BookRepository;
import com.project.book.common.exception.ContentNotFoundException;
import com.project.book.member.domain.Member;
import com.project.book.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private KeywordService keywordService;

    @Mock
    private ReadBookService readBookService;

    @InjectMocks
    private BookService bookService;

    @DisplayName("Kakao 데이터로 <읽은 책>을 저장할 때는, 해당 책을 books4dev(내 서비스)에 먼저 저장시킨 후 저장한다.")
    @Test
    void saveBookFromKakao() {
        // given
        Member member = Member.builder()
                .oAuth("123")
                .build();

        BookInfoDto bookInfoDto = BookInfoDto.builder()
                .isbn("11")
                .title("RealMySQL")
                .build();

        BookReviewDto bookReviewDto = BookReviewDto.builder()
                .searchKeyword("MySQL")
                .readTime(BookTime.threeYear)
                .recommendTime(BookTime.anyTime)
                .star(5).build();

        SaveBookFromSearchDto request = SaveBookFromSearchDto.builder()
                .info(bookInfoDto)
                .review(bookReviewDto).build();

        BookInfo bookInfo = BookInfo.builder()
                .isbn(bookInfoDto.getIsbn())
                .title(bookInfoDto.getTitle())
                .build();

        Book newBook = Book.builder()
                .id(1L)
                .bookInfo(bookInfo)
                .build();


        given(memberRepository.findByoAuth(member.getOAuth())).willReturn(member);
        given(bookRepository.findByIsbn(anyString())).willReturn(null);
        given(bookRepository.save(any(Book.class))).willReturn(newBook);
        willDoNothing().given(keywordService).incrementKeywordScore(anyLong(), anyString());

        // when
        bookService.saveBookFromKakao(member.getOAuth(), request);

        // then
        assertAll(
                () -> {
                    verify(memberRepository, times(1)).findByoAuth(member.getOAuth());
                    verify(bookRepository, times(1)).findByIsbn(anyString());
                    verify(bookRepository, times(1)).save(any(Book.class));
                    verify(keywordService, times(1)).incrementKeywordScore(newBook.getId(), bookReviewDto.getSearchKeyword());
                }
        );
    }

    @DisplayName("Kakao 데이터로 <읽은 책>을 저장을 시도하지만 이미 내 서비스에 저장된 경우, <읽은 책>만 저장한다.")
    @Test
    void saveBookFromKakao2() {
        // given
        Member member = Member.builder()
                .oAuth("123")
                .build();

        BookInfoDto bookInfoDto = BookInfoDto.builder()
                .isbn("11")
                .title("RealMySQL")
                .build();

        BookReviewDto bookReviewDto = BookReviewDto.builder()
                .searchKeyword("MySQL")
                .readTime(BookTime.threeYear)
                .recommendTime(BookTime.anyTime)
                .star(5).build();

        SaveBookFromSearchDto request = SaveBookFromSearchDto.builder()
                .info(bookInfoDto)
                .review(bookReviewDto).build();

        Book savedBook = Book.builder()
                .id(1L)
                .build();

        given(memberRepository.findByoAuth(member.getOAuth())).willReturn(member);
        given(bookRepository.findByIsbn(anyString())).willReturn(savedBook);
        willDoNothing().given(readBookService).saveReadBook(any(), any(), any());
        willDoNothing().given(readBookService).calculateAvgStar(any());
        willDoNothing().given(keywordService).incrementKeywordScore(anyLong(), anyString());

        // when
        bookService.saveBookFromKakao(member.getOAuth(), request);

        // then
        assertAll(
                () -> {
                    verify(memberRepository, times(1)).findByoAuth(member.getOAuth());
                    verify(bookRepository, times(1)).findByIsbn(anyString());
                    verify(readBookService, times(1)).saveReadBook(member, savedBook, bookReviewDto);
                    verify(readBookService, times(1)).calculateAvgStar(savedBook);
                    verify(keywordService, times(1)).incrementKeywordScore(savedBook.getId(), bookReviewDto.getSearchKeyword());
                }
        );
    }

    @DisplayName("Books4dev(내 서비스)에 저장된 책을 <읽은 책>으로 저장한다.")
    @Test
    void saveBookFromBooks4dev() {
        // given
        Member member = Member.builder()
                .oAuth("123")
                .build();

        String testIsbn = "00000";

        BookInfo bookInfo = BookInfo.builder()
                .isbn(testIsbn)
                .build();

        Book book = Book.builder()
                .id(1L)
                .bookInfo(bookInfo)
                .build();

        BookReviewDto bookReviewDto = BookReviewDto.builder()
                .readTime(BookTime.threeYear)
                .recommendTime(BookTime.anyTime)
                .star(5).build();

        SaveBookFromListDto request = SaveBookFromListDto.builder()
                .isbn(testIsbn)
                .review(bookReviewDto)
                .build();

        given(memberRepository.findByoAuth(member.getOAuth())).willReturn(member);
        given(bookRepository.findByIsbn(testIsbn)).willReturn(book);

        // when
        bookService.saveBookFromBooks4dev(member.getOAuth(), request);

        // then
        assertAll(
                () -> {
                    verify(memberRepository, times(1)).findByoAuth(member.getOAuth());
                    verify(bookRepository, times(1)).findByIsbn(testIsbn);
                }
        );
    }

    @DisplayName("Books4dev(내 서비스)에 저장된 책을 <읽은 책>으로 저장한다. 검색어가 존재할 경우, 검색어를 따로 저장한다")
    @Test
    void saveBookFromBooks4dev2() {
        // given
        Member member = Member.builder()
                .oAuth("123")
                .build();

        String testIsbn = "00000";

        BookInfo bookInfo = BookInfo.builder()
                .isbn(testIsbn)
                .build();

        Book book = Book.builder()
                .id(1L)
                .bookInfo(bookInfo)
                .build();

        BookReviewDto bookReviewDto = BookReviewDto.builder()
                .searchKeyword("MySQL")
                .readTime(BookTime.threeYear)
                .recommendTime(BookTime.anyTime)
                .star(5).build();

        SaveBookFromListDto request = SaveBookFromListDto.builder()
                .isbn(testIsbn)
                .review(bookReviewDto)
                .build();

        given(memberRepository.findByoAuth(member.getOAuth())).willReturn(member);
        given(bookRepository.findByIsbn(testIsbn)).willReturn(book);
        willDoNothing().given(keywordService).incrementKeywordScore(anyLong(), anyString());

        // when
        bookService.saveBookFromBooks4dev(member.getOAuth(), request);

        // then
        assertAll(
                () -> {
                    verify(memberRepository, times(1)).findByoAuth(member.getOAuth());
                    verify(bookRepository, times(1)).findByIsbn(testIsbn);
                    verify(keywordService, times(1)).incrementKeywordScore(book.getId(), bookReviewDto.getSearchKeyword());
                }
        );
    }

    @DisplayName("저장된 책들을 조회한다. totalCount를 모르면 count 쿼리가 발생한다.")
    @Test
    void getAllBook() {
        // given
        AllBookFilterDto condition = new AllBookFilterDto();

        Pageable pageable = new CustomPageRequest().toPageable();

        List<BookResponseDto> allBooks = Arrays.asList(
                new BookResponseDto(),
                new BookResponseDto()
        );

        Long totalCount = 2L;

        given(bookRepository.getAllBooks(condition, pageable)).willReturn(allBooks);
        given(bookRepository.countAllBooks(condition)).willReturn(totalCount);

        // when
        Page<?> result = bookService.getAllBook(condition, pageable);

        // then
        assertAll(
                () -> {
                    assertThat(result.getContent()).isEqualTo(allBooks);
                    assertThat(result.getTotalElements()).isEqualTo(totalCount);
                    verify(bookRepository, times(1)).getAllBooks(condition, pageable);
                    verify(bookRepository, times(1)).countAllBooks(condition);
                }
        );
    }

    @DisplayName("저장된 책들을 조회한다. totalCount를 알면 count 쿼리가 발생하지 않는다.")
    @Test
    void getAllBookWithCount() {
        // given
        Long totalCount = 2L;
        AllBookFilterDto condition = new AllBookFilterDto();
        condition.setTotalCount(totalCount);

        Pageable pageable = new CustomPageRequest().toPageable();

        List<BookResponseDto> allBooks = Arrays.asList(
                new BookResponseDto(),
                new BookResponseDto()
        );

        given(bookRepository.getAllBooks(condition, pageable)).willReturn(allBooks);

        // when
        Page<?> result = bookService.getAllBook(condition, pageable);

        // then
        assertAll(
                () -> {
                    assertThat(result.getContent()).isEqualTo(allBooks);
                    assertThat(result.getTotalElements()).isEqualTo(totalCount);
                    verify(bookRepository, times(1)).getAllBooks(condition, pageable);
                    verify(bookRepository, times(0)).countAllBooks(condition);

                }
        );
    }

    @DisplayName("text를 검색하면, 일치하는 책들을 조회한다.")
    @Test
    void findBookBySearch() {
        // given
        String text = "jpa";

        List<BookResponseDto> bookList = Arrays.asList(
                new BookResponseDto(),
                new BookResponseDto()
        );

        given(bookRepository.findBookBySearch(text)).willReturn(bookList);

        // when
        List<BookResponseDto> result = bookService.findBookBySearch(text);

        // then
        assertAll(
                () -> {
                    assertThat(result).isEqualTo(bookList);
                    verify(bookRepository, times(1)).findBookBySearch(text);
                }
        );
    }

    @DisplayName("저장된 책의 id를 통해서 자세한 정보를 조회한다.")
    @Test
    void getDetailBook() {
        // given
        Long id = 1L;

        BookInfo bookInfo = BookInfo.builder()
                .title("RealMysql")
                .isbn("123")
                .build();

        Book book = Book.builder()
                .id(id)
                .bookInfo(bookInfo)
                .build();

        List<KeywordScoreResponseDto> relatedKeywordList = Arrays.asList(
                new KeywordScoreResponseDto(),
                new KeywordScoreResponseDto()
        );

        given(bookRepository.findById(id)).willReturn(Optional.of(book));
        given(keywordService.getRelatedKeyword(id)).willReturn(relatedKeywordList);

        // when
        DetailBookResponseDto result = bookService.getDetailBook(id);

        // then
        assertAll(
                () -> {
                    assertThat(result.getId()).isEqualTo(id);
                    assertThat(result.getId()).isEqualTo(book.getId());
                    assertThat(result.getIsbn()).isEqualTo(book.getBookInfo().getIsbn());
                    assertThat(result.getTitle()).isEqualTo(book.getBookInfo().getTitle());
                    assertThat(result.getTopKeywordList()).isEqualTo(relatedKeywordList);
                    verify(bookRepository, times(1)).findById(id);
                    verify(keywordService, times(1)).getRelatedKeyword(id);
                }
        );
    }

    @DisplayName("존재하지 않는 책의 id로 조회하는 경우, 예외가 발생한다.")
    @Test
    void getDetailBookButFail() {
        // given
        Long id = 1L;

        given(bookRepository.findById(id)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> bookService.getDetailBook(id))
                .isInstanceOf(ContentNotFoundException.class)
                .hasMessage("찾을 수 없습니다.");
    }
}

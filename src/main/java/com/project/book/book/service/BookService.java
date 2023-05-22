package com.project.book.book.service;

import com.project.book.book.domain.*;
import com.project.book.book.dto.request.*;
import com.project.book.book.dto.response.*;
import com.project.book.book.repository.BookRepository;
import com.project.book.book.repository.ReadBookRepository;
import com.project.book.book.repository.WishBookRepository;
import com.project.book.common.exception.ContentNotFoundException;
import com.project.book.member.domain.Member;
import com.project.book.member.domain.MemberType;
import com.project.book.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final ReadBookRepository readBookRepository;
    private final WishBookRepository wishBookRepository;
    private final MemberRepository memberRepository;
    private final KeywordService keywordService;

    // Book 엔티티 처음 등록할 때는 카카오에서 보내준 데이터로 등록
    @Transactional
    public Book saveBookFromSearch(final String oAuth, final SaveBookFromSearchDto request) {
        Member member = memberRepository.findByoAuth(oAuth);

        String isbn = request.getInfo().getIsbn();
        Book savedBook = bookRepository.findByIsbn(isbn);

        // 예외인 상황, savedBook != null 인 상황
        // 1. 검색 화면에서 두 번 이상 등록할 때
        // 2. 검색어 불일치 때문에 검색 리스트에 등록된 책이 안 나올때
        if (savedBook != null) {
            saveReadBook(member, savedBook, request.getReview());
            calculateAvgStar(savedBook);
            saveKeyword(savedBook.getId(), request.getReview().getSearchKeyword());
            return savedBook;
        }

        Book tempbook = request.getInfo().toBook();
        tempbook.calculateAvgStar(request.getReview().getStar());

        Book newBook = bookRepository.save(tempbook);

        saveReadBook(member, newBook, request.getReview());
        saveKeyword(newBook.getId(), request.getReview().getSearchKeyword());
        return newBook;
    }

    public void saveKeyword(final Long bookId, final String keyword) {
        keywordService.incrementKeywordScore(bookId, keyword);
    }

    // 책 등록 (카카오 데이터로 등록 제외)
    @Transactional
    public Book saveBookFromList(final String oAuth, final SaveBookFromListDto request) {
        Member member = memberRepository.findByoAuth(oAuth);

        String isbn = request.getIsbn();
        Book savedBook = bookRepository.findByIsbn(isbn);

        saveReadBook(member, savedBook, request.getReview());
        calculateAvgStar(savedBook);

        if (request.getReview().getSearchKeyword() != null) {
            saveKeyword(savedBook.getId(), request.getReview().getSearchKeyword());
        }

        return savedBook;
    }

    // 읽은 책
    // 유저는 읽은 시기에 따라서 같은 책을 중복 등록할 수 있음
    private void saveReadBook(final Member member, final Book book, final BookReviewDto reviewDto) {
        // 유저, 책, 읽은 시기가 같은 걸 조회
        ReadBook readBook = readBookRepository.findByMemberAndBookAndReadTime(member, book, reviewDto.getReadTime());

        // 이미 등록된 적이 있지만 다시 저장 로직 수행(업데이트)
        if (readBook != null) {
            // 기존 추천시기 -1
            book.calculateRecommendTime(readBook.getRecommendBookTime(), -1);
            // 새로운 추천시기 +1
            book.calculateRecommendTime(reviewDto.getRecommendTime(), 1);
            readBook.updateReadBook(reviewDto.getStar(), reviewDto.getRecommendTime());
        }
        if (readBook == null) {
            ReadBook newReadBook = ReadBook.builder()
                                .book(book)
                                .member(member)
                                .readBookTime(reviewDto.getReadTime())
                                .recommendBookTime(reviewDto.getRecommendTime())
                                .star(reviewDto.getStar())
                                .build();

            book.calculateRecommendTime(newReadBook.getRecommendBookTime(), 1);
            book.calculateReadCount(1);
            readBookRepository.save(newReadBook);
        }
        return;
    }

    // readBook의 평점을 구해서 Book 엔티티에 저장
    private void calculateAvgStar(Book book) {
        Double avgStar = readBookRepository.findAvgStar(book);
        book.calculateAvgStar(avgStar);
    }

    @Transactional
    public ResponseEntity saveWishBook(final String oAuth, final BookInfoDto request) {
        Member member = memberRepository.findByoAuth(oAuth);

        String isbn = request.getIsbn();
        Book savedBook = bookRepository.findByIsbn(isbn);

        // 서비스에 등록이 안된 책일때, 책을 먼저 저장하고 진행
        if (savedBook == null) {
            Book tempBook = request.toBook();
            tempBook.calculateWishCount(1);
            Book newBook = bookRepository.save(tempBook);
            WishBook wishBook = new WishBook(member, newBook);
            wishBookRepository.save(wishBook);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }

        // 이미 등록됐을 때, 예외 던짐
        if (wishBookRepository.existByBookAndMember(savedBook, member)) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        savedBook.calculateWishCount(1);
        WishBook wishBook = new WishBook(member, savedBook);
        wishBookRepository.save(wishBook);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    public Page<?> getAllBook(final AllBookFilterDto condition, Pageable pageRequest) {
        if (condition.getMemberType() == null || condition.getMemberType().equals(MemberType.All)) {
            condition.setMemberType(null);
        }
        if (condition.getRecommendType() == null || condition.getRecommendType().equals(BookTime.All)) {
            condition.setRecommendType(null);
        }

        List<AllBookResponseDto> allBooks = bookRepository.getAllBooks(condition, pageRequest);
        Long totalCount = fetchTotalCount(condition);

        return new PageImpl<> (allBooks, pageRequest, totalCount);
    }

    private Long fetchTotalCount(AllBookFilterDto condition) {
        if (condition.getTotalCount() == null) {
            return bookRepository.countAllBooks(condition);
        }
        return condition.getTotalCount();
    }

    // 유저의 읽은 책 조회
    public ResponseEntity<?> getMemberReadBook(final Long id) {
        String oauth = String.valueOf(id);
        Member member = memberRepository.findByoAuth(oauth);
        Map<BookTime, List<ReadBookResponseDto>> bookTimeListMap = readBookRepository.getMemberReadBook(member);
        return new ResponseEntity<>(bookTimeListMap, HttpStatus.ACCEPTED);
    }

    // 유저 관심있는 책 조회
    public ResponseEntity<?> getMemberWishBook(final Long id) {
        String oauth = String.valueOf(id);
        Member member = memberRepository.findByoAuth(oauth);
        List<WishBookResponseDto> wishBook = wishBookRepository.getAllWishBook(member);
        return new ResponseEntity<>(wishBook, HttpStatus.ACCEPTED);
    }

    public List<AllBookResponseDto> findBookBySearch(final String text) {
        return bookRepository.findBookBySearch(text);
    }

    public BookResponseDto getDetailBook(final Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new ContentNotFoundException());
//        BookTimeCount readTime = readBookRepository.getReadTime(book);
        List<KeywordScoreResponseDto> relatedKeyword = keywordService.getRelatedKeyword(id);
        return BookResponseDto.from(book, relatedKeyword);
    }
}
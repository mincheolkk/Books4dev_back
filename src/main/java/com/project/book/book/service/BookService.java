package com.project.book.book.service;

import com.project.book.book.domain.*;
import com.project.book.book.dto.request.*;
import com.project.book.book.dto.response.*;
import com.project.book.book.repository.BookRepository;
import com.project.book.book.repository.ReadBookRepository;
import com.project.book.book.repository.WishBookRepository;
import com.project.book.common.utils.RedisUtil;
import com.project.book.member.domain.Member;
import com.project.book.member.domain.MemberType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.project.book.book.domain.ReadBook.toReadBook;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final ReadBookRepository readBookRepository;
    private final WishBookRepository wishBookRepository;
    private final RedisUtil redisUtil;

    // Book 엔티티 처음 등록할 때는 카카오에서 보내준 데이터로 등록
    @Transactional
    public Book saveFromKakao(final SaveBookFromKakaoDto request, final Member member) {

        // 검색 화면에서 두 번 이상 등록할 때
        // 검색어 불일치 때문에 검색 리스트에 등록된 책이 안 나올때

        String isbn = request.getItem().getIsbn();
        Book savedBook = bookRepository.findByIsbn(isbn);
        if (savedBook != null) {
            saveReadBook(member, savedBook, request.getReview());
            calculateAvgStar(savedBook);
            saveKeyword(savedBook.getId(), request.getReview().getSearchKeyword());
            return savedBook;
        }

        Book tempbook = request.toBook(request.getItem());
        tempbook.calculateAvgStar(request.getReview().getStar());

        Book newBook = bookRepository.save(tempbook);

        saveReadBook(member, newBook, request.getReview());
        saveKeyword(newBook.getId(), request.getReview().getSearchKeyword());
        return newBook;
    }

    public void saveKeyword(final Long id, String keyword) {
        redisUtil.incrementKeywordScore(id, keyword);
    }

    // 책 등록 (처음 등록 제외)
    @Transactional
    public Book saveBookFromBooks4dev(final SaveBookDto request, final Member member) {
        String isbn = request.getIsbn();
        Book savedBook = bookRepository.findByIsbn(isbn);

        saveReadBook(member, savedBook, request.getReview());
        calculateAvgStar(savedBook);

        if (request.getReview().getSearchKeyword() != null) {
            saveKeyword(savedBook.getId(), request.getReview().getSearchKeyword());
        }

        return savedBook;
    }

    public void saveReadBook(final Member member, final Book book, final BookReviewDto reviewDto) {
        // 유저, 책, 읽은 시기가 같은 걸 조회
        ReadBook readBook = readBookRepository.findByMemberAndBookAndReadTime(member, book, reviewDto.getReadTime());

        // 등록된 읽은 시기에 대해 재요청
        if (readBook != null) {
            // 기존 추천시기 -1
            book.calculateRecommendTime(readBook.getRecommendBookTime(), -1);
            // 새로운 추천시기 +1
            book.calculateRecommendTime(reviewDto.getRecommendTime(), 1);
            readBook.updateReadBook(reviewDto.getStar(), reviewDto.getRecommendTime());
            readBookRepository.save(readBook);
        }
        if (readBook == null) {
            ReadBook newReadBook = toReadBook(book, reviewDto, member);
            book.calculateRecommendTime(newReadBook.getRecommendBookTime(), 1);
            book.plusReadCount(1);
            readBookRepository.save(newReadBook);
        }
        return;
    }

    // RegisterBook 의 평점 값을 Book 으로
    public void calculateAvgStar(Book book) {
        Double avgStar = readBookRepository.findAvgStar(book);
        book.calculateAvgStar(avgStar);
    }

    @Transactional
    public ResponseEntity saveWishBook(final BookDataDto request, final Member member) {
        String isbn = request.getIsbn();
        Book savedBook = bookRepository.findByIsbn(isbn);

        if (!request.validCheck()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        if (savedBook == null) {
            Book tempBook = request.toBook();
            tempBook.plusWishCount();
            Book newBook = bookRepository.save(tempBook);
            WishBook wishBook = new WishBook(member, newBook);
            wishBookRepository.save(wishBook);
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }

        // 요청하는 유저가 해당 책으로 이미 관심있는 책을 등록했을때
        if (wishBookRepository.existByBookAndMember(savedBook, member)) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        savedBook.plusWishCount();
        WishBook wishBook = new WishBook(member, savedBook);
        wishBookRepository.save(wishBook);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    public ResponseEntity<?> getAllBook(final AllBookFilterDto condition, Pageable pageRequest) {
        if (condition.getMemberType() == null || condition.getMemberType().equals(MemberType.All)) {
            condition.setMemberType(null);
        }
        if (condition.getRecommendType() == null || condition.getRecommendType().equals(BookTime.All)) {
            condition.setRecommendType(null);
        }
        return new ResponseEntity<>(bookRepository.getAllBooks(condition, pageRequest), HttpStatus.ACCEPTED);
    }

    public ResponseEntity<?> getMyWishBook(final Member member) {
        List<WishBookResponseDto> wishBook = wishBookRepository.getAllWishBook(member);
        return new ResponseEntity<>(wishBook, HttpStatus.ACCEPTED);
    }

    public ResponseEntity<?> getMyReadBook(final Member member) {
        Map<BookTime, List<ReadBookResponseDto>> bookTimeListMap = readBookRepository.getMyReadBook(member);
        return new ResponseEntity<>(bookTimeListMap, HttpStatus.ACCEPTED);
    }

    public List<AllBookResponseDto> findBookBySearch(final String text) {
        return bookRepository.findBookBySearch(text);
    }

    public BookResponseDto getDetailBook(final Long id) {
        Book book = bookRepository.findById(id).get();
        List<KeywordScoreResponseDto> topThree = redisUtil.getKeyword(id);
        return BookResponseDto.from(book, topThree);
    }
    public ResponseEntity<?> getPopularKeyword() {
        List<KeywordScoreResponseDto> topThree = redisUtil.getTopThree();
        return new ResponseEntity<>(topThree, HttpStatus.ACCEPTED);
    }
}
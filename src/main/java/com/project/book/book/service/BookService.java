package com.project.book.book.service;

import com.project.book.book.domain.*;
import com.project.book.book.dto.request.*;
import com.project.book.book.dto.response.*;
import com.project.book.book.repository.BookRepository;
import com.project.book.book.repository.RegisterBookRepository;
import com.project.book.book.repository.WishBookRepository;
import com.project.book.book.repository.WishMemberRepository;
import com.project.book.member.domain.Member;
import com.project.book.member.domain.MemberType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final RegisterBookRepository registerBookRepository;
    private final WishBookRepository wishBookRepository;
    private final WishMemberRepository wishMemberRepository;

    // Book 엔티티 처음 등록할 때
    @Transactional
    public Book firstSaveBook(final FirstSaveBookDto request, final Member member) {

        // 검색 화면에서 두 번 이상 등록할 때, 등록은 됐지만 검색어 불일치 때문에 안 나올때
        String isbn = request.getItem().getIsbn();
        Book savedBook = bookRepository.findByIsbn(isbn);
        if (savedBook != null) {
            saveRegisterBook(member, savedBook, request.getReview());
            calculateAvgStar(savedBook);
            return savedBook;
        }

        Book tempbook = request.toBook(request.getItem());
        tempbook.calculateAvgStar(request.getReview().getStar());

        Book newBook = bookRepository.save(tempbook);

        saveRegisterBook(member, newBook, request.getReview());
        getWishBookCount(isbn, newBook);

        return newBook;
    }

    // 책 등록 (처음 등록 제외)
    @Transactional
    public Book saveBook(final SaveBookDto request, final Member member) {
        String isbn = request.getIsbn();
        Book savedBook = bookRepository.findByIsbn(isbn);
        saveRegisterBook(member, savedBook, request.getReview());
        calculateAvgStar(savedBook);

        return savedBook;
    }

    public void saveRegisterBook(final Member member, final Book book, final BookReviewDto reviewDto) {
        // 유저, 책, 읽은 시기가 같은 걸 조회
        RegisterBook registerbook = registerBookRepository.findByMemberAndBookAndReadTime(member, book, reviewDto.getReadTime());

        // 등록된 읽은 시기에 대해 재요청
        if (registerbook != null) {
            // 기존 추천시기 -1
            book.calculateRecommendTime(registerbook.getRecommendBookTime(), -1);
            // 새로운 추천시기 +1
            book.calculateRecommendTime(reviewDto.getRecommendTime(), 1);
            registerbook.updateRegisterBook(reviewDto.getStar(), reviewDto.getRecommendTime());
            registerBookRepository.save(registerbook);
        }
        if (registerbook == null) {
            RegisterBook registerBook = toRegisterBook(book, reviewDto, member);
            book.calculateRecommendTime(registerBook.getRecommendBookTime(), 1);
            book.plusRegisterCount(1);
            registerBookRepository.save(registerBook);
        }
        return;
    }

    // RegisterBook 의 평점 값을 Book 으로
    public void calculateAvgStar(Book book) {
        Double avgStar = registerBookRepository.findAvgStar(book);
        book.calculateAvgStar(avgStar);
    }

    private static RegisterBook toRegisterBook(final Book book, final BookReviewDto request, final Member member) {
        return RegisterBook.builder()
                .book(book)
                .readBookTime(request.getReadTime())
                .recommendBookTime(request.getRecommendTime())
                .star(request.getStar())
                .member(member)
                .build();
    }

    @Transactional
    public ResponseEntity saveWishBook(final WishBookRequestDto request, final Member member) {
        String isbn = request.getIsbn();
        WishBook wishBook = wishBookRepository.findByIsbn(isbn);
        Book savedBook = bookRepository.findByIsbn(isbn);

        if (wishBook == null) {
            WishBook wish = request.toEntity();
            wishBookRepository.save(wish);

            saveWishMember(member, wish);
            if (savedBook != null) {
                savedBook.plusWishCount();
            }

           return new ResponseEntity(HttpStatus.ACCEPTED);
        }

        // 요청하는 유저가 해당 책으로 이미 관심있는 책을 등록했을때
        if (wishMemberRepository.findByWishBook(wishBook, member)) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        saveWishMember(member, wishBook);
        if (savedBook != null) {
            savedBook.plusWishCount();
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    public void saveWishMember(final Member member, final WishBook wishBook) {
        WishMember wishMember = WishMember.builder()
                .wishBook(wishBook)
                .member(member)
                .build();
        wishMemberRepository.save(wishMember);
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
        List<WishBookResponseDto> wishBook = wishMemberRepository.getAllWishBook(member);
        return new ResponseEntity<>(wishBook, HttpStatus.ACCEPTED);
    }

    public ResponseEntity<?> getMyReadBook(final Member member) {
        Map<BookTime, List<ReadBookResponseDto>> bookTimeListMap = registerBookRepository.getMyReadBook(member);
        return new ResponseEntity<>(bookTimeListMap, HttpStatus.ACCEPTED);
    }

    public void getWishBookCount(final String isbn, final Book savedBook) {
        long wishBookCount = wishMemberRepository.findWishBookCount(isbn);
        savedBook.getWishCount((int) wishBookCount);
    }

    public List<AllBookResponseDto> findBookBySearch(final String text) {
        return bookRepository.findBookBySearch(text);
    }

    public BookResponseDto getDetailBook(final Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return BookResponseDto.from(book.get());
    }
}
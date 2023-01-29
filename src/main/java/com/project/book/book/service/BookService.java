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

    @Transactional
    public Book registerBySearch(final RegisterBySearchDto request, final Member member) {

        String isbn = request.getItem().getIsbn();
        Book savedBook = bookRepository.findByIsbn(isbn);

        if (savedBook == null) {
            Book tempbook = request.toBook(request.getItem());
            tempbook.plusRegisterCount(1);
            tempbook.plusRecommendTime(request.getReview().getRecommendTime(), 1);
            tempbook.calculateAvgStar(request.getReview().getStar());

            Book newBook = bookRepository.save(tempbook);

            findRegisterBookForUpdate(member, newBook, request.getReview());
            findWishBookCount(isbn);

            return newBook;
        }
        if (savedBook != null) {
            findRegisterBookForUpdate(member, savedBook, request.getReview());
            starCountRecommend(savedBook, request.getReview());
            findWishBookCount(isbn);
        }
        return savedBook;
    }

    public void findRegisterBookForUpdate(final Member member, final Book book, final BookReviewDto reviewDto) {
        RegisterBook registerbook = registerBookRepository.findByMemberAndBookAndReadTime(member, book, reviewDto.getReadTime());

        if (registerbook != null) {
            registerbook.updateRegisterBook(reviewDto.getStar(), reviewDto.getRecommendTime());
            registerBookRepository.save(registerbook);
            return;
        }
        if (registerbook == null) {
            RegisterBook registerBook = requestRegisterBook(book, reviewDto, member);
            registerBookRepository.save(registerBook);
            return;
        }
    }

    @Transactional
    public Book registerByHomeList(final RegisterByHomeListDto request, final Member member) {
        String isbn = request.getIsbn();
        Book savedBook = bookRepository.findByIsbn(isbn);
        findWishBookCount(isbn);
        findRegisterBookForUpdate(member, savedBook, request.getReview());
        starCountRecommend(savedBook, request.getReview());

        return savedBook;
    }

    public void starCountRecommend(Book book, BookReviewDto request) {
        List<RecommendCountDto> byRecommendCount = registerBookRepository.findRecommendCount(book);
        long registerCount = 0;
        book.zeroRecommendTime();
        for (RecommendCountDto dto : byRecommendCount) {
            book.plusRecommendTime(dto.getBookTime(), dto.getCount());
            registerCount += dto.getCount();
        }
        book.plusRegisterCount(registerCount);
        Double avgStar = registerBookRepository.findAvgStar(book);
        book.calculateAvgStar(avgStar);

    }
    public void findRegistCountnRecommendCount() {

    }

    private static RegisterBook requestRegisterBook(final Book book, final BookReviewDto request, final Member member) {
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


        if (wishBook == null) {
            WishBook wish = request.toEntity();

            wishBookRepository.save(wish);

           saveWishMember(member, wish);
           findWishBookCount(isbn);
           return new ResponseEntity(HttpStatus.ACCEPTED);
        }
        boolean flag = wishMemberRepository.findByWishBook(wishBook, member);
        if (flag) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        saveWishMember(member, wishBook);
        findWishBookCount(isbn);
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

    public void findWishBookCount(final String isbn) {
        Book savedBook = bookRepository.findByIsbn(isbn);
        if (savedBook == null) {
            return;
        }

        long wishBookCount = wishMemberRepository.findWishBookCount(isbn);

        savedBook.plusWishCount((int) wishBookCount);
    }

    public List<AllBookResponseDto> findBookBySearch(final String text) {
        return bookRepository.findBookBySearch(text);
    }

    public BookResponseDto getDetailBook(final Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return BookResponseDto.from(book.get());
    }
}
package com.project.book.book.service;

import com.project.book.book.domain.*;
import com.project.book.book.dto.request.*;
import com.project.book.book.dto.response.*;
import com.project.book.book.repository.BookRepository;
import com.project.book.book.repository.ReadBookRepository;
import com.project.book.book.repository.WishBookRepository;
import com.project.book.common.config.aop.DistributedLock;
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
    private final MemberRepository memberRepository;
    private final KeywordService keywordService;
    private final ReadBookService readBookService;

    // Book 엔티티 처음 등록할 때는 카카오에서 보내준 데이터로 등록
    @DistributedLock(key = "saveFromSearch")
    public Book saveBookFromSearch(final String oAuth, final SaveBookFromSearchDto request) {
        Member member = memberRepository.findByoAuth(oAuth);

        String isbn = request.getInfo().getIsbn();
        Book savedBook = bookRepository.findByIsbn(isbn);

        // 예외인 상황, savedBook != null 인 상황
        // 1. 검색 화면에서 두 번 이상 등록할 때
        // 2. 검색어 불일치 때문에 검색 리스트에 등록된 책이 안 나올때
        if (savedBook != null) {
            readBookService.saveReadBook(member, savedBook, request.getReview());
            readBookService.calculateAvgStar(savedBook);
            keywordService.incrementKeywordScore(savedBook.getId(), request.getReview().getSearchKeyword());
            return savedBook;
        }

        Book tempbook = request.getInfo().toBook();
        tempbook.calculateAvgStar(request.getReview().getStar());

        Book newBook = bookRepository.save(tempbook);

        readBookService.saveReadBook(member, newBook, request.getReview());
        keywordService.incrementKeywordScore(newBook.getId(), request.getReview().getSearchKeyword());
        return newBook;
    }

    // 책 등록 (카카오 데이터로 등록 제외)
    @DistributedLock(key = "saveFromList")
    public Book saveBookFromList(final String oAuth, final SaveBookFromListDto request) {
        Member member = memberRepository.findByoAuth(oAuth);

        String isbn = request.getIsbn();
        Book savedBook = bookRepository.findByIsbn(isbn);

        readBookService.saveReadBook(member, savedBook, request.getReview());
        readBookService.calculateAvgStar(savedBook);

        if (request.getReview().getSearchKeyword() != null) {
            keywordService.incrementKeywordScore(savedBook.getId(), request.getReview().getSearchKeyword());
        }

        return savedBook;
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

        return new PageImpl<>(allBooks, pageRequest, totalCount);
    }

    private Long fetchTotalCount(AllBookFilterDto condition) {
        if (condition.getTotalCount() == null) {
            return bookRepository.countAllBooks(condition);
        }
        return condition.getTotalCount();
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
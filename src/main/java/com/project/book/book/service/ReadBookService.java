package com.project.book.book.service;

import com.project.book.book.domain.Book;
import com.project.book.book.domain.BookTime;
import com.project.book.book.domain.ReadBook;
import com.project.book.book.dto.request.BookReviewDto;
import com.project.book.book.dto.response.ReadBookResponseDto;
import com.project.book.book.repository.ReadBookRepository;
import com.project.book.member.domain.Member;
import com.project.book.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ReadBookService {

    private final ReadBookRepository readBookRepository;
    private final MemberRepository memberRepository;

    public void saveReadBook(final Member member, final Book book, final BookReviewDto reviewDto) {
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

    public void calculateAvgStar(Book book) {
        Double avgStar = readBookRepository.findAvgStar(book);
        book.calculateAvgStar(avgStar);
    }

    // 유저의 읽은 책 조회
    public ResponseEntity<?> getMemberReadBook(final Long id) {
        String oauth = String.valueOf(id);
        Member member = memberRepository.findByoAuth(oauth);
        Map<BookTime, List<ReadBookResponseDto>> bookTimeListMap = readBookRepository.getMemberReadBook(member);
        return new ResponseEntity<>(bookTimeListMap, HttpStatus.OK);
    }

}

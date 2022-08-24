package com.project.book.book.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.book.book.domain.*;
import com.project.book.book.dto.request.*;
import com.project.book.book.dto.response.ReadBookResponseDto;
import com.project.book.book.dto.response.RecommendCountDto;
import com.project.book.book.dto.response.WishBookResponseDto;
import com.project.book.book.repository.BookRepository;
import com.project.book.book.repository.RegisterBookRepository;
import com.project.book.book.repository.WishBookRepository;
import com.project.book.book.repository.WishMemberRepository;
import com.project.book.member.domain.Member;
import com.project.book.member.domain.MemberType;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.project.book.book.domain.BookTime.*;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final RegisterBookRepository registerBookRepository;
    private final WishBookRepository wishBookRepository;
    private final WishMemberRepository wishMemberRepository;
    @Transactional
    public Book registerBySearch(RegisterBySearchDto request, Member member) {

        String isbn = request.getItem().getIsbn();
        Book savedBook = bookRepository.findByIsbn(isbn);
        findWishBookCount(isbn);

        if (savedBook == null) {
            CreateBookRequestDto createbook = CreateBookRequestDto.builder()
                    .authors(listToString(request.getItem().getAuthors()))
                    .translator(listToString(request.getItem().getTranslator()))
                    .title(request.getItem().getTitle())
                    .publisher(request.getItem().getPublisher())
                    .price(request.getItem().getPrice())
                    .thumbnail(request.getItem().getThumbnail())
                    .datetime(request.getItem().getDatetime().toLocalDateTime())
                    .isbn(isbn)
                    .build();

            Book tempbook = createbook.toEntity();
            tempbook.plusRegisterCount(1);
            tempbook.plusRecommendTime(request.getReview().getRecommendTime(), 1);
            tempbook.calculateAvgStar(request.getReview().getStar());
//            starCountRecommend(tempbook, request.getReview());

            Book newBook = bookRepository.save(tempbook);

            findRegisterBookForUpdate(member, newBook, request.getReview());
//            RegisterBook registerBook = requestRegisterBook(newBook, request.getReview(), member);
//            registerBookRepository.save(registerBook);

            return newBook;
        }
        else if (savedBook != null) {
            findRegisterBookForUpdate(member, savedBook, request.getReview());
            starCountRecommend(savedBook, request.getReview());
        }
        return savedBook;
    }

    public void findRegisterBookForUpdate(Member member, Book book, BookReviewDto reviewDto) {
        RegisterBook findedBook = registerBookRepository.findByMemberAndBookAndReadTime(member, book, reviewDto.getReadTime());

        if (findedBook != null) {
            findedBook.updateRegisterBook(reviewDto.getStar(), reviewDto.getRecommendTime());
            registerBookRepository.save(findedBook);
            return;
        } else if (findedBook == null) {
            RegisterBook registerBook = requestRegisterBook(book, reviewDto, member);
            registerBookRepository.save(registerBook);

            return;
        }
    }

    @Transactional
    public Book registerByHomeList(RegisterByHomeListDto request, Member member) {
        Book savedBook = bookRepository.findByIsbn(request.getIsbn());
//        RegisterBook registerBook = requestRegisterBook(savedBook, request.getReview(), member);
//        registerBookRepository.save(registerBook);
        findWishBookCount(request.getIsbn());
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

//        book.plusRecommendTime(request.getRecommendTime());
    }
    public void findRegistCountnRecommendCount() {

    }

    private static RegisterBook requestRegisterBook(Book book, BookReviewDto request, Member member) {
        return RegisterBook.builder()
                .book(book)
                .readBookTime(request.getReadTime())
                .recommendBookTime(request.getRecommendTime())
                .star(request.getStar())
                .member(member)
                .build();
    }

    @Transactional
    public ResponseEntity saveWishBook(WishBookRequestDto request, Member member) {
        WishBook wishBook = wishBookRepository.findByIsbn(request.getIsbn());
        findWishBookCount(request.getIsbn());

        if (wishBook == null) {
            WishBook wish = WishBook.builder()
                    .isbn(request.getIsbn())
                    .title(request.getTitle())
                    .thumbnail(request.getThumbnail())
                    .build();
            wishBookRepository.save(wish);

           saveWishMember(member, wish);
           return new ResponseEntity(HttpStatus.ACCEPTED);
        }

        boolean flag = wishMemberRepository.findByWishBook(wishBook, member);
        if (flag) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        saveWishMember(member, wishBook);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    public void saveWishMember(Member member, WishBook wishBook) {
        WishMember wishMember = WishMember.builder()
                .wishBook(wishBook)
                .member(member)
                .build();
        wishMemberRepository.save(wishMember);
    }

    public Map<String, Object> getDetailBook(Long id) throws JsonProcessingException {
        Optional<Book> book = bookRepository.findById(id);
        Map<String, Object> detailBook = bookRepository.getDetailBook(book.get());

        return detailBook;
    }

    private static String listToString(List<String> list) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return Strings.EMPTY;
        }
        return String.join(",", list);
    }

    public ResponseEntity<?> getAllBook(AllBookFilterDto condition, Pageable pageRequest) {
        if (condition.getMemberType() == null || condition.getMemberType().equals(MemberType.All)) {
            condition.setMemberType(null);
        }
        if (condition.getRecommendType() == null || condition.getRecommendType().equals(BookTime.All)) {
            condition.setRecommendType(null);
        }
        return new ResponseEntity<>(bookRepository.getAllBooks(condition, pageRequest), HttpStatus.ACCEPTED);
    }

    public ResponseEntity<?> getAllWishBook(Member member) {
        List<WishBookResponseDto> allWishBook = wishMemberRepository.getAllWishBook(member);
        return new ResponseEntity<>(allWishBook, HttpStatus.ACCEPTED);
    }

    public ResponseEntity<?> testReadBook(Member member) {
        HashMap readTimeMap = new HashMap<>();
        List<BookTime> enumList = Arrays.asList(before, after, twoYear, fiveYear, tenYear);
        for (BookTime bookTime : enumList) {
            List<ReadBookResponseDto> responseDtoList = registerBookRepository.testReadbook(member, bookTime);
            readTimeMap.put(bookTime, responseDtoList);
        }
        return new ResponseEntity<>(readTimeMap, HttpStatus.ACCEPTED);
    }

    public void findWishBookCount(String isbn) {
        Book savedBook = bookRepository.findByIsbn(isbn);
        if (savedBook == null) {
            return;
        }

        long wishBookCount = wishMemberRepository.findWishBookCount(isbn);
        System.out.println("wishBookCount = " + wishBookCount);

        savedBook.plusWishCount((int) wishBookCount);
    }


}
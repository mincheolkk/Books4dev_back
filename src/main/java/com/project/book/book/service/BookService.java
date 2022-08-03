package com.project.book.book.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.book.book.domain.*;
import com.project.book.book.dto.request.*;
import com.project.book.book.dto.response.AllBookResponseDto;
import com.project.book.book.dto.response.ReadBookResponseDto;
import com.project.book.book.dto.response.WishBookResponseDto;
import com.project.book.book.repository.BookRepository;
import com.project.book.book.repository.RegisterBookRepository;
import com.project.book.book.repository.WishBookRepository;
import com.project.book.book.repository.WishMemberRepository;
import com.project.book.member.domain.Member;
import com.project.book.member.domain.MemberType;
import com.project.book.member.repository.MemberRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
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
            starCountRecommend(tempbook, request.getReview());

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
        System.out.println("findedBook = " + findedBook);
        if (findedBook != null) {
            findedBook.updateRegisterBook(reviewDto.getStar());
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
        findRegisterBookForUpdate(member, savedBook, request.getReview());
        starCountRecommend(savedBook, request.getReview());

        return savedBook;
    }

    public void starCountRecommend(Book book, BookReviewDto request) {
        book.calculateAvgStar(request.getStar());
        book.plusRegisterCount();
        book.plusRecommendTime(request.getRecommendTime());

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

}
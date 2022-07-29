package com.project.book.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.book.book.domain.Book;
import com.project.book.book.domain.BookTime;
import com.project.book.book.domain.RegisterBook;
import com.project.book.book.repository.BookRepository;
import com.project.book.book.repository.RegisterBookRepository;
import com.project.book.book.service.BookService;
import com.project.book.member.domain.Member;
import com.project.book.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@SpringBootTest
public class BookServiceTest {

    @Autowired
    BookService bookService;
    @Autowired BookRepository repository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RegisterBookRepository registerBookRepository;

    @Test
    @Commit
    public void 책등록() throws Exception {

        //given

//        List<String> testAuthors = new ArrayList<>();
//        testAuthors.add("김작가1");
//        testAuthors.add("이작가2");
//        System.out.println("testAuthors = " + testAuthors);
//
//
//        BookRequestDto requsetDto = BookRequestDto.builder()
//                .datetime(ZonedDateTime.now())
//                .authors(testAuthors)
//                .translator(null)
//                .isbn("123131313")
//                .title("모던 자바 인 액션")
//                .price(30000L)
//                .thumbnail("url:test")
//                .publisher("한빛미디어")
//                .readTime(BookTime.fiveYear)
//                .recommendTime(BookTime.before)
//                .star(1)
//                .build();
//
//        BookRequestDto requsetDto2 = BookRequestDto.builder()
//                .datetime(ZonedDateTime.now())
//                .authors(testAuthors)
//                .translator(null)
//                .isbn("999999")
//                .title("객체지향의 사실과 오해")
//                .price(20000L)
//                .thumbnail("url:test")
//                .publisher("위키북스")
//                .readTime(BookTime.before)
//                .recommendTime(BookTime.after)
//                .star(5)
//                .build();
//
//        Book orRegisterBook = bookService.createOrRegisterBook(requsetDto);
//        bookService.createOrRegisterBook(requsetDto2);

        //when

        //then
    }

    @Test
    void 책디테일뷰() throws JsonProcessingException {

        Optional<Book> byId = repository.findById(1L);

        repository.getDetailBook(byId.get());
//        repository.testListCount(byId.get());
    }

//    @Test
//    void 읽은시간리스트뽑기() throws JsonProcessingException {
//        Optional<Book> byId = repository.findById(1L);
//        System.out.println("++++++++++++");
//
//        repository.testListCount(byId.get());
//    }

    @Test
    @Commit
    void registerDataTest() {
        Optional<Book> book1 = repository.findById(1L);
        Optional<Book> book2 = repository.findById(2L);

        Optional<Member> member1 = memberRepository.findById(1L);
        Optional<Member> member2 = memberRepository.findById(2L);
        Optional<Member> member3 = memberRepository.findById(3L);
        Optional<Member> member4 = memberRepository.findById(4L);

        registerBookRepository.save(RegisterBook.builder()
                .member(member1.get())
                .book(book1.get())
                .recommendBookTime(BookTime.after)
                .readBookTime(BookTime.before)
                .star(4)
                .build());

        registerBookRepository.save(RegisterBook.builder()
                .member(member1.get())
                .book(book2.get())
                .recommendBookTime(BookTime.twoYear)
                .readBookTime(BookTime.fiveYear)
                .star(3)
                .build());

        registerBookRepository.save(RegisterBook.builder()
                .member(member2.get())
                .book(book1.get())
                .recommendBookTime(BookTime.twoYear)
                .readBookTime(BookTime.twoYear)
                .star(5)
                .build());

        registerBookRepository.save(RegisterBook.builder()
                .member(member2.get())
                .book(book2.get())
                .recommendBookTime(BookTime.fiveYear)
                .readBookTime(BookTime.after)
                .star(3)
                .build());

        registerBookRepository.save(RegisterBook.builder()
                .member(member3.get())
                .book(book1.get())
                .recommendBookTime(BookTime.twoYear)
                .readBookTime(BookTime.after)
                .star(2)
                .build());

        registerBookRepository.save(RegisterBook.builder()
                .member(member3.get())
                .book(book2.get())
                .recommendBookTime(BookTime.fiveYear)
                .readBookTime(BookTime.before)
                .star(4)
                .build());

        registerBookRepository.save(RegisterBook.builder()
                .member(member3.get())
                .book(book1.get())
                .recommendBookTime(BookTime.after)
                .readBookTime(BookTime.after)
                .star(5)
                .build());

        registerBookRepository.save(RegisterBook.builder()
                .member(member1.get())
                .book(book2.get())
                .recommendBookTime(BookTime.before)
                .readBookTime(BookTime.after)
                .star(2)
                .build());
    }

    @Test
    void help() {
        bookService.hepll(1L);
    }
}

package com.project.book.book.service;

import com.project.book.book.domain.Book;
import com.project.book.book.domain.WishBook;
import com.project.book.book.dto.request.BookInfoDto;
import com.project.book.book.dto.response.WishBookResponseDto;
import com.project.book.book.repository.BookRepository;
import com.project.book.book.repository.WishBookRepository;
import com.project.book.common.config.aop.DistributedLock;
import com.project.book.member.domain.Member;
import com.project.book.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WishBookService {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final WishBookRepository wishBookRepository;

    @DistributedLock(key = "saveWishBook")
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
            return new ResponseEntity(HttpStatus.OK);
        }

        // 이미 등록됐을 때, 예외 던짐
        if (wishBookRepository.existByBookAndMember(savedBook, member)) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        savedBook.calculateWishCount(1);
        WishBook wishBook = new WishBook(member, savedBook);
        wishBookRepository.save(wishBook);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 유저 관심있는 책 조회
    @Transactional(readOnly = true)
    public ResponseEntity<?> getMemberWishBook(final Long id) {
        String oauth = String.valueOf(id);
        Member member = memberRepository.findByoAuth(oauth);
        List<WishBookResponseDto> wishBook = wishBookRepository.getAllWishBook(member);
        return new ResponseEntity<>(wishBook, HttpStatus.OK);
    }
}

package com.project.book.book.repository;

import com.project.book.book.domain.WishBook;
import com.project.book.member.domain.Member;

import java.util.Optional;

public interface WishBookRepositoryCustom  {
    WishBook findByIsbn(String isbn);

}
